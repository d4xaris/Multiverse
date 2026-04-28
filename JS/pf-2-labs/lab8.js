class AuthProxy {
  constructor(strategy, { rateLimit = 5, windowMs = 10000 } = {}) {
    this.strategy = strategy;
    this.rateLimit = rateLimit;
    this.windowMs = windowMs;
    this.requestLog = [];
  }

  setStrategy(strategy) {
    this.strategy = strategy;
  }

  isRateLimited() {
    const now = Date.now();
    this.requestLog = this.requestLog.filter((t) => now - t < this.windowMs);
    return this.requestLog.length >= this.rateLimit;
  }

  async request(url, options = {}) {
    if (this.isRateLimited()) {
      console.warn(
        `[proxy] rate limit reached (${this.rateLimit} req / ${this.windowMs}ms)`,
      );
      throw new Error("Rate limit exceeded");
    }

    this.requestLog.push(Date.now());

    const headers = await this.strategy.getHeaders();
    const finalOptions = {
      ...options,
      headers: { ...options.headers, ...headers },
    };
    const method = finalOptions.method || "GET";

    console.log(
      `[proxy] ${method} ${url} | total requests: ${this.requestLog.length}`,
    );

    const res = await fetch(url, finalOptions);

    if (res.status === 401) {
      console.log(`[proxy] 401 received, refreshing token...`);
      await this.strategy.refresh?.();
      const retryHeaders = await this.strategy.getHeaders();
      return fetch(url, {
        ...finalOptions,
        headers: { ...options.headers, ...retryHeaders },
      });
    }

    return res;
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

  async refresh() {
    console.log(`[jwt] refreshing token...`);
    this.token = "refreshed-token-" + Date.now();
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

  async refresh() {
    console.log(`[oauth] refreshing token...`);
    await this.fetchToken();
  }
}

async function main() {
  const proxy = new AuthProxy(new ApiKeyStrategy("my-secret-api-key"), {
    rateLimit: 3,
    windowMs: 5000,
  });

  await proxy.request("https://jsonplaceholder.typicode.com/posts/1");

  proxy.setStrategy(new JwtStrategy("eyJhbGciOiJIUzI1NiJ9.mock"));
  await proxy.request("https://jsonplaceholder.typicode.com/posts/2");

  proxy.setStrategy(new OAuthStrategy("client-123", "secret-456"));
  await proxy.request("https://jsonplaceholder.typicode.com/posts/3");

  try {
    await proxy.request("https://jsonplaceholder.typicode.com/posts/4");
  } catch (e) {
    console.error(`[main] ${e.message}`);
  }
}

main().catch(console.error);
