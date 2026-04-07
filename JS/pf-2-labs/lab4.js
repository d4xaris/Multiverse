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
  _rebuildIndices() {
    if (this._data.length === 0) {
      this._maxIdx = this._minIdx = -1;
      return;
    }
    this._maxIdx = 0;
    this._minIdx = 0;
    for (let i = 1; i < this._data.length; i++) {
      if (this._data[i].priority > this._data[this._maxIdx].priority)
        this._maxIdx = i;
      if (this._data[i].priority < this._data[this._minIdx].priority)
        this._minIdx = i;
    }
  }

  _removeAt(index) {
    const [removed] = this._data.splice(index, 1);
    this._rebuildIndices();
    return removed.item;
  }

  dequeueHighest() {
    if (this.isEmpty()) return null;
    return this._removeAt(this._maxIdx);
  }

  dequeueLowest() {
    if (this.isEmpty()) return null;
    return this._removeAt(this._minIdx);
  }

  dequeueOldest() {
    if (this.isEmpty()) return null;
    let oldestIdx = 0;
    for (let i = 1; i < this._data.length; i++) {
      if (this._data[i].insertionIndex < this._data[oldestIdx].insertionIndex)
        oldestIdx = i;
    }
    return this._removeAt(oldestIdx);
  }

  dequeueNewest() {
    if (this.isEmpty()) return null;
    return this._removeAt(this._data.length - 1);
  }

  peekHighest() {
    return this.isEmpty() ? null : this._data[this._maxIdx].item;
  }
  peekLowest() {
    return this.isEmpty() ? null : this._data[this._minIdx].item;
  }
  peekNewest() {
    return this.isEmpty() ? null : this._data[this._data.length - 1].item;
  }
  peekOldest() {
    if (this.isEmpty()) return null;
    let oldest = this._data[0];
    for (const n of this._data)
      if (n.insertionIndex < oldest.insertionIndex) oldest = n;
    return oldest.item;
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
        throw new Error(`Unknown mode: "${mode}"`);
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
pq.enqueue("A", 3);
pq.enqueue("B", 1);
pq.enqueue("C", 5);
pq.enqueue("D", 2);

console.log(pq.dequeueHighest());
console.log(pq.dequeueLowest());
console.log(pq.dequeueOldest());
console.log(pq.dequeueNewest());
console.log(pq.size);
