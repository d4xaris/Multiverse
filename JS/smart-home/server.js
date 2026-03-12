const net = require("net");

const server = net.createServer((socket) => {
  console.log("client connected");

  socket.on("data", (buf) => {
    const cmd = buf[0];

    if (cmd === 0x01)
      console.log("heating:", buf.readUInt32BE(1) ? "on" : "off");
    if (cmd === 0x02)
      console.log("door:", buf.readUInt32BE(1) ? "opened" : "closed");
    if (cmd === 0x03) console.log("feeding:", buf.readFloatBE(1), "kg");
    if (cmd === 0x04) {
      console.log("temperature requested, sent: 22.5");
      socket.write(reply(0x04, 22.5, true));
    }
    if (cmd === 0x05) {
      console.log("food requested, sent: yes");
      socket.write(reply(0x05, 1));
    }
  });

  socket.on("end", () => console.log("client disconnected"));
});

function reply(cmd, value, isFloat = false) {
  const buf = Buffer.alloc(5);
  buf.writeUInt8(cmd, 0);
  isFloat ? buf.writeFloatBE(value, 1) : buf.writeUInt32BE(value, 1);
  return buf;
}

server.listen(3000, () => console.log("server running on port 3000"));
