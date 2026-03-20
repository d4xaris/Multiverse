function memoize(fn, options = {}) {
    const cache = new Map();

    const maxSize = options.maxSize || Infinity;
    const policy = options.policy || "LRU";

    function evictOne() {
        if (cache.size === 0) return;

        let keyToDelete;

        if (policy === "LRU") {
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

        const key = JSON.stringify(args);
        const now = Date.now();

        if (cache.has(key)) {
            const item = cache.get(key);

            item.lastUsed = now;

            return item.result;
        }

        const result = fn(...args);

        if (cache.size >= maxSize) {
            evictOne();
        }

        cache.set(key, {
            result: result,
            lastUsed: now,
        });

        return result;
    };
}

const memoizedPow = memoize((a, b) => {
    console.log("calculating");
    return a ** b;
}, {
    maxSize: 2,
    policy: "LRU",
});

console.log(memoizedPow(2, 3));
console.log(memoizedPow(2, 3));
console.log(memoizedPow(3, 3));
console.log(memoizedPow(4, 4));
console.log(memoizedPow(2, 3));
