class BiDirectionalPriorityQueue {
  constructor() {
    this._data = [];
    this._insertionCounter = 0;
    this._maxIdx = -1;
    this._minIdx = -1;
    this._oldestIdx = -1;
  }

  enqueue(item, priority) {
    const node = { item, priority, insertionIndex: this._insertionCounter++ };
    this._data.push(node);
    const i = this._data.length - 1;

    if (
      this._maxIdx === -1 ||
      node.priority > this._data[this._maxIdx].priority
    )
      this._maxIdx = i;
    if (
      this._minIdx === -1 ||
      node.priority < this._data[this._minIdx].priority
    )
      this._minIdx = i;
    if (
      this._oldestIdx === -1 ||
      node.insertionIndex < this._data[this._oldestIdx].insertionIndex
    )
      this._oldestIdx = i;
  }

  _rebuildIndices() {
    if (this._data.length === 0) {
      this._maxIdx = this._minIdx = this._oldestIdx = -1;
      return;
    }
    this._maxIdx = this._minIdx = this._oldestIdx = 0;
    for (let i = 1; i < this._data.length; i++) {
      const n = this._data[i];
      if (n.priority > this._data[this._maxIdx].priority) this._maxIdx = i;
      if (n.priority < this._data[this._minIdx].priority) this._minIdx = i;
      if (n.insertionIndex < this._data[this._oldestIdx].insertionIndex)
        this._oldestIdx = i;
    }
  }

  _removeAt(index) {
    const [removed] = this._data.splice(index, 1);
    this._rebuildIndices();
    return removed.item;
  }

  dequeue(mode) {
    if (this.isEmpty()) return null;
    switch (mode) {
      case "highest":
        return this._removeAt(this._maxIdx);
      case "lowest":
        return this._removeAt(this._minIdx);
      case "oldest":
        return this._removeAt(this._oldestIdx);
      case "newest":
        return this._removeAt(this._data.length - 1);
      default:
        throw new Error(`Unknown dequeue mode: "${mode}"`);
    }
  }

  peek(mode) {
    if (this.isEmpty()) return null;
    switch (mode) {
      case "highest":
        return this._data[this._maxIdx].item;
      case "lowest":
        return this._data[this._minIdx].item;
      case "oldest":
        return this._data[this._oldestIdx].item;
      case "newest":
        return this._data[this._data.length - 1].item;
      default:
        throw new Error(`Unknown peek mode: "${mode}"`);
    }
  }

  get size() {
    return this._data.length;
  }
  isEmpty() {
    return this._data.length === 0;
  }
}

const pq = new BiDirectionalPriorityQueue();
pq.enqueue("email", 2);
pq.enqueue("meeting", 5);
pq.enqueue("report", 1);
pq.enqueue("call", 4);
pq.enqueue("lunch", 3);

console.log("=== peek ===");
console.log("highest:", pq.peek("highest"));
console.log("lowest:", pq.peek("lowest"));
console.log("oldest:", pq.peek("oldest"));
console.log("newest:", pq.peek("newest"));
console.log("\n=== dequeue ===");
console.log(pq.dequeue("highest"));
console.log(pq.dequeue("lowest"));
console.log(pq.dequeue("oldest"));
console.log(pq.dequeue("newest"));
console.log("remaining:", pq.size);
console.log(pq.dequeue("highest"));
console.log("empty:", pq.isEmpty());
