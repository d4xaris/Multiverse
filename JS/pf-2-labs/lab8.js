const https = require("https");

class AuthProxy {
  constructor(strategy) {
    this.strategy = strategy;
  }

  setStrategy(strategy) {
    this.strategy = strategy;
  }

  async request(url, options = {}) {
    const headers = await this.strategy.getHeaders();
    const finalOptions = {
      ...options,
      headers: { ...options.headers, ...headers },
    };

    console.log(`[proxy] ${finalOptions.method || "GET"} ${url}`);
    console.log(`[proxy] headers:`, finalOptions.headers);

    return fetch(url, finalOptions);
  }
}

class ApiKeyStrategy {
  constructor(apiKey) {
    this.apiKey = apiKey;
  }

  async getHeaders() {
    return { "x-api-key": this.apiKey };
  }
}

class JwtStrategy {
  constructor(token) {
    this.token = token;
  }

  async getHeaders() {
    return { Authorization: `Bearer ${this.token}` };
  }
}

class OAuthStrategy {
  constructor(clientId, clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.token = null;
  }

  async fetchToken() {
    console.log(`[oauth] fetching token for client: ${this.clientId}`);
    this.token = "mock-oauth-token-" + Date.now();
  }

  async getHeaders() {
    if (!this.token) await this.fetchToken();
    return { Authorization: `Bearer ${this.token}` };
  }
}

async function main() {
  const proxy = new AuthProxy(new ApiKeyStrategy("my-secret-api-key"));
  await proxy.request("https://jsonplaceholder.typicode.com/posts/1");

  proxy.setStrategy(new JwtStrategy("eyJhbGciOiJIUzI1NiJ9.mock"));
  await proxy.request("https://jsonplaceholder.typicode.com/posts/2");

  proxy.setStrategy(new OAuthStrategy("client-123", "secret-456"));
  await proxy.request("https://jsonplaceholder.typicode.com/posts/3");
}

main().catch(console.error);
