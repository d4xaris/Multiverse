const { EventEmitter } = require("events");

const messageBus = new EventEmitter();

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

messageBus.on("message", logger);
messageBus.on("message", alertSystem);
messageBus.on("message", dashboard);

messageBus.emit("message", { from: "serviceA", text: "hello world" });
messageBus.emit("message", {
  from: "serviceB",
  text: "error: something broke",
});

console.log("\n-- unsubscribing dashboard --\n");
messageBus.off("message", dashboard);

messageBus.emit("message", {
  from: "serviceA",
  text: "dashboard won't see this",
});
