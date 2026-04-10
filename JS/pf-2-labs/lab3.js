function memoize(fn, options = {}) {
  const cache = new Map();

  const maxSize = options.maxSize || Infinity;
  const policy = options.policy || "LRU";
  const ttl = options.ttl || 0;
  const customEviction = options.customEviction || null;

  function removeExpired() {
    if (ttl <= 0) return;

    const now = Date.now();

    for (const [key, item] of cache) {
      if (now - item.createdAt > ttl) {
        cache.delete(key);
      }
    }
  }

  function evictOne() {
    if (cache.size === 0) return;

    let keyToDelete;

    if (policy === "LFU") {
      let minCount = Infinity;

      for (const [key, item] of cache) {
        if (item.count < minCount) {
          minCount = item.count;
          keyToDelete = key;
        }
      }
    } else if (policy === "CUSTOM" && typeof customEviction === "function") {
      keyToDelete = customEviction(cache);
    } else if (policy === "LRU") {
      let oldestTime = Infinity;

      for (const [key, item] of cache) {
        if (item.lastUsed < oldestTime) {
          oldestTime = item.lastUsed;
          keyToDelete = key;
        }
      }
    }

    if (keyToDelete !== undefined) {
      cache.delete(keyToDelete);
    }
  }

  return function (...args) {
    removeExpired();

    const key = JSON.stringify(args);
    const now = Date.now();

    if (cache.has(key)) {
      const item = cache.get(key);

      item.lastUsed = now;
      item.count++;

      return item.result;
    }

    const result = fn(...args);

    if (cache.size >= maxSize) {
      evictOne();
    }

    cache.set(key, {
      result: result,
      createdAt: now,
      lastUsed: now,
      count: 1,
    });

    return result;
  };
}

const memoizedPow = memoize(
  (a, b) => {
    console.log("calculating");
    return a ** b;
  },
  {
    maxSize: 2,
    policy: "LRU",
  },
);

console.log("LRU ==================");
console.log(memoizedPow(2, 1000));
console.log(memoizedPow(2, 1000));
console.log(memoizedPow(4, 4));
console.log(memoizedPow(3, 3));
console.log(memoizedPow(2, 1000));

const memoizedSum = memoize(
  (a, b) => {
    console.log("calculating");
    return a + b;
  },
  {
    maxSize: 2,
    policy: "LFU",
  },
);

console.log("LFU ==================");
console.log(memoizedSum(2, 1000));
console.log(memoizedSum(2, 1000));
console.log(memoizedSum(4, 4));
console.log(memoizedSum(3, 3));
console.log(memoizedSum(4, 4));

const memoizedSquare = memoize(
  (x) => {
    console.log("calculating");
    return x * x;
  },
  {
    ttl: 2000,
  },
);

console.log("TTL ==================");
console.log(memoizedSquare(5));
console.log(memoizedSquare(5));

setTimeout(() => {
  console.log(memoizedSquare(5));
}, 3000);
