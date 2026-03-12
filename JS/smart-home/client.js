const net = require("net");

const client = net.createConnection({ port: 3000 }, () => {
  console.log("connected to server");

  send(0x01, 1);
  setTimeout(() => send(0x02, 0), 100);
  setTimeout(() => send(0x03, 0.5, true), 200);
  setTimeout(() => send(0x04, 0), 300);
  setTimeout(() => send(0x05, 0), 400);
});

function send(cmd, value, isFloat = false) {
  const buf = Buffer.alloc(5);
  buf.writeUInt8(cmd, 0);
  isFloat ? buf.writeFloatBE(value, 1) : buf.writeUInt32BE(value, 1);
  client.write(buf);
}

client.on("data", (buf) => {
  const cmd = buf.readUInt8(0);
  if (cmd === 0x04) console.log("temperature:", buf.readFloatBE(1));
  if (cmd === 0x05) console.log("food:", buf.readUInt32BE(1) ? "yes" : "no");
});
