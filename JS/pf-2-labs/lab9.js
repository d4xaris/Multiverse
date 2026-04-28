const LEVELS = { DEBUG: 0, INFO: 1, ERROR: 2 };

let activeLevel = LEVELS.DEBUG;

function setLogLevel(level) {
  activeLevel = LEVELS[level];
}

function log(level = "INFO") {
  return function (fn) {
    return async function (...args) {
      const shouldLog = LEVELS[level] >= activeLevel;

      if (shouldLog && level !== "ERROR") {
        console.log(
          `[${timestamp()}] [${level}] ${fn.name}() called with:`,
          args,
        );
      }

      try {
        const result = await fn(...args);

        if (shouldLog && level !== "ERROR") {
          console.log(
            `[${timestamp()}] [${level}] ${fn.name}() returned:`,
            result,
          );
        }

        return result;
      } catch (err) {
        if (level === "ERROR") {
          console.error(
            `[${timestamp()}] [ERROR] ${fn.name}() threw:`,
            err.message,
          );
        }
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

  console.log("\n-- setting active level to ERROR --\n");
  setLogLevel("ERROR");

  await getUser(2);

  try {
    await divide(10, 0);
  } catch (_) {}
}

main().catch(console.error);
