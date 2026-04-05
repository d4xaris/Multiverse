class BiDirectionalPriorityQueue {
  constructor() {
    this._data = [];
    this._insertionCounter = 0;
    this._maxIdx = -1;
    this._minIdx = -1;
  }

  enqueue(item, priority) {
    const node = { item, priority, insertionIndex: this._insertionCounter++ };
    this._data.push(node);
    this._updateIndicesAfterInsert(this._data.length - 1);
  }

  _updateIndicesAfterInsert(newIdx) {
    const node = this._data[newIdx];
    if (
      this._maxIdx === -1 ||
      node.priority > this._data[this._maxIdx].priority
    )
      this._maxIdx = newIdx;
    if (
      this._minIdx === -1 ||
      node.priority < this._data[this._minIdx].priority
    )
      this._minIdx = newIdx;
  }

  peekHighest() {
    if (this.isEmpty()) return null;
    return this._data[this._maxIdx].item;
  }

  peekLowest() {
    if (this.isEmpty()) return null;
    return this._data[this._minIdx].item;
  }

  peekOldest() {
    if (this.isEmpty()) return null;
    let oldest = this._data[0];
    for (let i = 1; i < this._data.length; i++) {
      if (this._data[i].insertionIndex < oldest.insertionIndex)
        oldest = this._data[i];
    }
    return oldest.item;
  }

  peekNewest() {
    if (this.isEmpty()) return null;
    return this._data[this._data.length - 1].item;
  }

  peek(mode) {
    switch (mode) {
      case "highest":
        return this.peekHighest();
      case "lowest":
        return this.peekLowest();
      case "oldest":
        return this.peekOldest();
      case "newest":
        return this.peekNewest();
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
pq.enqueue("task A", 3);
pq.enqueue("task B", 1);
pq.enqueue("task C", 5);

console.log(pq.peek("highest"));
console.log(pq.peek("lowest"));
console.log(pq.peek("oldest"));
console.log(pq.peek("newest"));
