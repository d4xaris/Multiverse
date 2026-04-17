function asyncMapPromise(arr, asyncTransform) {
  return new Promise(function (resolve, reject) {
    const promises = [];

    for (let i = 0; i < arr.length; i++) {
      promises.push(asyncTransform(arr[i]));
    }

    Promise.all(promises)
      .then(function (results) {
        resolve(results);
      })
      .catch(function (err) {
        reject(err);
      });
  });
}

function doublePromise(num) {
  return new Promise(function (resolve) {
    setTimeout(function () {
      resolve(num * 2);
    }, 100);
  });
}

console.log("Demo 1: Doubling numbers with Promises");

asyncMapPromise([1, 2, 3, 4, 5], doublePromise)
  .then(function (results) {
    console.log("Input:  [1, 2, 3, 4, 5]");
    console.log("Output:", results);
  })
  .catch(function (err) {
    console.error("Error:", err.message);
  });

const fakeDatabase = {
  1: "Alice",
  2: "Bob",
  3: "Charlie",
};

function getUserName(id) {
  return new Promise(function (resolve, reject) {
    setTimeout(function () {
      const name = fakeDatabase[id];
      if (name) {
        resolve(name);
      } else {
        reject(new Error("User " + id + " not found"));
      }
    }, 80);
  });
}

console.log("\nDemo 2: Fetching user names by ID");

asyncMapPromise([1, 2, 3], getUserName)
  .then(function (names) {
    console.log("Input:  [1, 2, 3]");
    console.log("Output:", names);
  })
  .catch(function (err) {
    console.error("Error:", err.message);
  });

console.log("\nDemo 3: One ID is missing from the database");

asyncMapPromise([1, 99, 3], getUserName)
  .then(function (names) {
    console.log("Output:", names);
  })
  .catch(function (err) {
    console.error("Caught error:", err.message);
  });

console.log("\nDemo 4: Chaining — double, then convert to strings");

asyncMapPromise([5, 10, 15], doublePromise)
  .then(function (doubled) {
    return asyncMapPromise(doubled, function (num) {
      return new Promise(function (resolve) {
        setTimeout(function () {
          resolve("Value: " + num);
        }, 50);
      });
    });
  })
  .then(function (labelled) {
    console.log("Output:", labelled);
  })
  .catch(function (err) {
    console.error("Error:", err.message);
  });
