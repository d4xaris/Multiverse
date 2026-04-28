const fs = require("fs");

const LEVELS = { DEBUG: 0, INFO: 1, ERROR: 2 };

let activeLevel = LEVELS.DEBUG;
let formatter = defaultFormatter;
const outputs = [consoleOutput];

function setLogLevel(level) {
  activeLevel = LEVELS[level];
}

function setFormatter(fn) {
  formatter = fn;
}

function addFileOutput(filePath) {
  outputs.push((entry) => fs.appendFileSync(filePath, formatter(entry) + "\n"));
}

function defaultFormatter(entry) {
  return `[${entry.timestamp}] [${entry.level}] ${entry.fn}() | args: ${JSON.stringify(entry.args)} | result: ${JSON.stringify(entry.result ?? entry.error)} | ${entry.ms}ms`;
}

function jsonFormatter(entry) {
  return JSON.stringify(entry);
}

function consoleOutput(entry) {
  if (entry.error) {
    console.error(formatter(entry));
  } else {
    console.log(formatter(entry));
  }
}

function write(entry) {
  outputs.forEach((out) => out(entry));
}

function log(level = "INFO") {
  return function (fn) {
    return async function (...args) {
      if (LEVELS[level] < activeLevel) return fn(...args);

      const start = Date.now();

      try {
        const result = await fn(...args);

        if (level !== "ERROR") {
          write({
            timestamp: timestamp(),
            level,
            fn: fn.name,
            args,
            result,
            ms: Date.now() - start,
          });
        }

        return result;
      } catch (err) {
        write({
          timestamp: timestamp(),
          level: "ERROR",
          fn: fn.name,
          args,
          error: err.message,
          ms: Date.now() - start,
        });
        throw err;
      }
    };
  };
}

function timestamp() {
  return new Date().toISOString();
}

// --- example usage ---

const getUser = log("INFO")(async function getUser(id) {
  return { id, name: "Alice" };
});

const divide = log("ERROR")(async function divide(a, b) {
  if (b === 0) throw new Error("division by zero");
  return a / b;
});

const debugAdd = log("DEBUG")(async function debugAdd(a, b) {
  return a + b;
});

async function main() {
  await getUser(1);
  await debugAdd(3, 4);

  console.log("\n-- switching to JSON formatter + file output --\n");
  setFormatter(jsonFormatter);
  addFileOutput("./lab9.log");

  await getUser(2);

  try {
    await divide(10, 0);
  } catch (_) {}

  console.log("\n-- setting active level to ERROR --\n");
  setLogLevel("ERROR");

  await getUser(3);

  try {
    await divide(5, 0);
  } catch (_) {}
}

main().catch(console.error);
