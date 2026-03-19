function memoize(fn) {
    const cache = new Map();

    return function (...args) {

        const key = JSON.stringify(...args);

        if (cache.has(key)) {
            return cache.get(key);
        }

        const result = fn(...args);

        cache.set(key, result);

        return result;
    };
}

const memoizedPow = memoize((a, b) => {
    console.log("calculating");
    return a ** b;
});

console.log(memoizedPow(2, 3));
console.log(memoizedPow(2, 3));
console.log(memoizedPow(3, 3));
