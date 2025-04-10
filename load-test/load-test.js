import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter } from 'k6/metrics';

// Custom metrics
export const successfulRequests = new Counter('successful_requests');
export const failedRequests = new Counter('failed_requests');
export const responseTime = new Trend('response_time');

// Load test config
export const options = {
  vus: 50, // virtual users
  duration: '30s', // total duration
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  const name = `team-lunch-${Math.random().toString(36).substring(7)}`;
  const payload = JSON.stringify({
    name: name,
    createdBy: 'alice@example.com',
  });

  const headers = { 'Content-Type': 'application/json' };
  const res = http.post(`${BASE_URL}/api/v1/expense/groups`, payload, { headers });

  const success = check(res, {
    'status is 201': (r) => r.status === 201,
  });

  responseTime.add(res.timings.duration);

  if (success) {
    successfulRequests.add(1);
  } else {
    failedRequests.add(1);
  }

  sleep(0.1); // sleep to simulate real user pacing
}
