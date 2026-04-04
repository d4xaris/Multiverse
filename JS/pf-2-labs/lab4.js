class BiDirectionalPriorityQueue {
  constructor() {
    this._data = [];
    this._insertionCounter = 0;
  }

  enqueue(item, priority) {
    const node = {
      item,
      priority,
      insertionIndex: this._insertionCounter++,
    };
    this._data.push(node);
  }

  get size() {
    return this._data.length;
  }

  isEmpty() {
    return this._data.length === 0;
  }

  _maxPriorityIndex() {
    let best = 0;
    for (let i = 1; i < this._data.length; i++) {
      if (this._data[i].priority > this._data[best].priority) best = i;
    }
    return best;
  }

  _minPriorityIndex() {
    let best = 0;
    for (let i = 1; i < this._data.length; i++) {
      if (this._data[i].priority < this._data[best].priority) best = i;
    }
    return best;
  }
}

const pq = new BiDirectionalPriorityQueue();
pq.enqueue("task A", 3);
pq.enqueue("task B", 1);
pq.enqueue("task C", 5);
console.log("Size:", pq.size);
