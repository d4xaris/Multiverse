async function* generateRows(totalRows) {
  for (let i = 0; i < totalRows; i++) {
    yield {
      id: i,
      value: Math.random() * 1000,
      label: `item-${i}`,
    };

    if (i % 10_000 === 0) await new Promise((r) => setImmediate(r));
  }
}

async function* filterAndEnrich(source) {
  let seen = 0;
  let passed = 0;

  for await (const row of source) {
    seen++;

    if (row.value < 500) continue;

    passed++;
    yield {
      ...row,
      value: parseFloat(row.value.toFixed(2)),
      processed: true,
    };
  }

  console.log(`\nFilter stats: ${passed}/${seen} rows passed`);
}

async function run() {
  const TOTAL_ROWS = 1_000_000;

  console.log(
    `Processing ${TOTAL_ROWS.toLocaleString()} rows with async iterators...\n`,
  );

  const source = generateRows(TOTAL_ROWS);
  const processed = filterAndEnrich(source);

  let count = 0;

  for await (const row of processed) {
    count++;

    if (count % 100_000 === 0) {
      console.log(
        `  processed ${count.toLocaleString()} rows | latest id: ${row.id}`,
      );
    }
  }

  console.log(`\nDone. Total rows after filter: ${count.toLocaleString()}`);
}

run().catch(console.error);
