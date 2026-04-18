async function asyncMap(arr, asyncTransform) {
  const results = [];

  for (let i = 0; i < arr.length; i++) {
    const value = await asyncTransform(arr[i]);
    results.push(value);
  }

  return results;
}

function doublePromise(num) {
  return new Promise(function (resolve) {
    setTimeout(function () {
      resolve(num * 2);
    }, 100);
  });
}

async function runAsyncAwaitDemos() {
  console.log("Demo 1: Doubling with async/await");
  const doubled = await asyncMap([1, 2, 3, 4, 5], doublePromise);
  console.log("Input:  [1, 2, 3, 4, 5]");
  console.log("Output:", doubled);

  console.log("\nDemo 2: Error handling with try/catch");

  async function mightFail(num) {
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        if (num > 3) {
          reject(new Error("Number " + num + " is too big!"));
        } else {
          resolve(num * 100);
        }
      }, 60);
    });
  }

  try {
    const results = await asyncMap([1, 2, 5], mightFail);
    console.log("Output:", results);
  } catch (err) {
    console.error("Caught:", err.message);
  }

  console.log("\nDemo 3: Chaining steps cleanly");

  const step1 = await asyncMap([10, 20, 30], doublePromise);
  console.log("After doubling:", step1);

  const step2 = await asyncMap(step1, function (n) {
    return new Promise(function (resolve) {
      setTimeout(function () {
        resolve("$" + n);
      }, 40);
    });
  });
  console.log("After labelling:", step2);
}

runAsyncAwaitDemos();

async function asyncMapAbortable(arr, asyncTransform, signal) {
  const results = [];

  for (let i = 0; i < arr.length; i++) {
    if (signal && signal.aborted) {
      throw new Error("Map was aborted before item " + i);
    }

    const value = await asyncTransform(arr[i], signal);
    results.push(value);
  }

  return results;
}

function slowDouble(num, signal) {
  return new Promise(function (resolve, reject) {
    const timer = setTimeout(function () {
      resolve(num * 2);
    }, 300);

    if (signal) {
      signal.addEventListener("abort", function () {
        clearTimeout(timer);
        reject(new Error("Aborted while processing " + num));
      });
    }
  });
}

async function runAbortDemos() {
  console.log("\nDemo 4: Aborting the map halfway through");

  const controller = new AbortController();

  setTimeout(function () {
    console.log(">>> Sending abort signal!");
    controller.abort();
  }, 500);

  try {
    const results = await asyncMapAbortable(
      [1, 2, 3, 4, 5],
      slowDouble,
      controller.signal,
    );
    console.log("Output:", results);
  } catch (err) {
    console.error("Map stopped early:", err.message);
  }

  console.log("\nDemo 5: Aborting before the map starts");

  const controller2 = new AbortController();
  controller2.abort();

  try {
    const results = await asyncMapAbortable(
      [10, 20, 30],
      slowDouble,
      controller2.signal,
    );
    console.log("Output:", results);
  } catch (err) {
    console.error("Map stopped early:", err.message);
  }

  console.log("\nDemo 6: Abortable map without aborting (runs fine)");

  const controller3 = new AbortController();

  try {
    const results = await asyncMapAbortable(
      [1, 2, 3],
      slowDouble,
      controller3.signal,
    );
    console.log("Input:  [1, 2, 3]");
    console.log("Output:", results); // [2, 4, 6]
  } catch (err) {
    console.error("Unexpected error:", err.message);
  }
}

runAbortDemos();
