class Observable {
  constructor() {
    this.listeners = {};
  }

  subscribe(event, fn) {
    if (!this.listeners[event]) this.listeners[event] = [];
    this.listeners[event].push(fn);
  }

  unsubscribe(event, fn) {
    if (!this.listeners[event]) return;
    this.listeners[event] = this.listeners[event].filter((l) => l !== fn);
  }

  emit(event, data) {
    if (!this.listeners[event]) return;
    this.listeners[event].forEach((fn) => fn(data));
  }
}

const messageBus = new Observable();

function logger(event) {
  console.log(`[logger] received: "${event.text}" from ${event.from}`);
}

function alertSystem(event) {
  if (event.text.includes("error")) {
    console.log(`[alert] ERROR detected from ${event.from}!`);
  }
}

function dashboard(event) {
  console.log(`[dashboard] new message at ${new Date().toLocaleTimeString()}`);
}

messageBus.subscribe("message", logger);
messageBus.subscribe("message", alertSystem);
messageBus.subscribe("message", dashboard);

messageBus.emit("message", { from: "serviceA", text: "hello world" });
messageBus.emit("message", {
  from: "serviceB",
  text: "error: something broke",
});

console.log("\n-- unsubscribing dashboard --\n");
messageBus.unsubscribe("message", dashboard);

messageBus.emit("message", {
  from: "serviceA",
  text: "dashboard won't see this",
});
