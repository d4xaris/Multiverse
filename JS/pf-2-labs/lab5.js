function syncMap(arr, transform) {
  const result = [];
  for (let i = 0; i < arr.length; i++) {
    result.push(transform(arr[i]));
  }
  return result;
}

function asyncMapCallback(arr, asyncTransform, finalCallback) {
  const results = [];
  let completed = 0;

  if (arr.length === 0) {
    finalCallback(null, []);
    return;
  }

  for (let i = 0; i < arr.length; i++) {
    (function (index) {
      asyncTransform(arr[index], function (err, value) {
        if (err) {
          finalCallback(err, null);
          return;
        }

        results[index] = value;
        completed++;

        if (completed === arr.length) {
          finalCallback(null, results);
        }
      });
    })(i);
  }
}

function doubleAsync(num, callback) {
  setTimeout(function () {
    callback(null, num * 2);
  }, 100);
}

console.log("Demo 1: Doubling numbers asynchronously");

asyncMapCallback([1, 2, 3, 4, 5], doubleAsync, function (err, results) {
  if (err) {
    console.error("Something went wrong:", err);
    return;
  }
  console.log("Input:  [1, 2, 3, 4, 5]");
  console.log("Output:", results);
});

function toUpperAsync(str, callback) {
  setTimeout(function () {
    callback(null, str.toUpperCase());
  }, 50);
}

console.log("\nDemo 2: Uppercasing strings asynchronously");

asyncMapCallback(
  ["hello", "world", "foo"],
  toUpperAsync,
  function (err, results) {
    if (err) {
      console.error("Error:", err);
      return;
    }
    console.log("Input:  ['hello', 'world', 'foo']");
    console.log("Output:", results);
  },
);

function mightFailAsync(num, callback) {
  setTimeout(function () {
    if (num === 3) {
      callback(new Error("Oops, 3 is not allowed!"), null);
    } else {
      callback(null, num * 10);
    }
  }, 80);
}

console.log("\nDemo 3: Handling an error mid-map");

asyncMapCallback([1, 2, 3, 4], mightFailAsync, function (err, results) {
  if (err) {
    console.error("Caught error:", err.message);
    return;
  }
  console.log("Output:", results);
});
