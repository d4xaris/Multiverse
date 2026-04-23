const { Readable, Transform, pipeline } = require("stream");
const { promisify } = require("util");

const pipelineAsync = promisify(pipeline);

class LargeDataSource extends Readable {
  constructor(totalRows) {
    super({ objectMode: true });
    this.totalRows = totalRows;
    this.currentRow = 0;
  }

  _read() {
    if (this.currentRow >= this.totalRows) {
      this.push(null);
      return;
    }

    this.push({
      id: this.currentRow,
      value: Math.random() * 1000,
      label: `item-${this.currentRow}`,
    });

    this.currentRow++;
  }
}

class FilterAndEnrich extends Transform {
  constructor() {
    super({ objectMode: true });
    this.seen = 0;
    this.passed = 0;
  }

  _transform(row, _encoding, done) {
    this.seen++;

    if (row.value < 500) {
      done();
      return;
    }

    this.passed++;
    this.push({
      ...row,
      value: parseFloat(row.value.toFixed(2)),
      processed: true,
    });

    done();
  }

  _flush(done) {
    console.log(`\nFilter stats: ${this.passed}/${this.seen} rows passed`);
    done();
  }
}

async function run() {
  const TOTAL_ROWS = 1_000_000;

  console.log(`Streaming ${TOTAL_ROWS.toLocaleString()} rows...\n`);

  const source = new LargeDataSource(TOTAL_ROWS);
  const filter = new FilterAndEnrich();

  let processedCount = 0;

  const filtered = source.pipe(filter);

  for await (const row of filtered) {
    processedCount++;

    if (processedCount % 100_000 === 0) {
      console.log(
        `  processed ${processedCount.toLocaleString()} rows so far | latest id: ${row.id}`,
      );
    }
  }

  console.log(
    `\nDone. Total rows processed after filter: ${processedCount.toLocaleString()}`,
  );
}

run().catch(console.error);
