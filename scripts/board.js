import http from 'k6/http';
import {check, sleep} from 'k6';

// K6_INFLUXDB_PUSH_INTERVAL=5s TEST_TYPE=create k6 run --out influxdb=http://localhost:8086 board.js

export const options = {
    stages: [
        {duration: '30s', target: 1000},
        {duration: '1m', target: 5000},
        {duration: '30s', target: 0},
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = 'http://localhost:8091/api/board';
const AUTH_URL = 'http://localhost:8091/api/auth/access';
const USERNAME = 'string1';
const PASSWORD = 'string';
const refreshToken = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmcxIiwiZXhwIjoxNzM3MTY2NTU0LCJyYW5kb20iOiJjZDhmYjg1NC1jYmYyLTRhYzEtYjUwNS03ZTgyYTc4MDllOTkifQ.1r-SlKBMxs-SNQeUBeLrVbur4ZvC_6UPoTpF2ikQh-iw0gcqd9TezYK-UE06dD3QG_nHeiDSQu37KAKVts_BGA';

// JWT 토큰 발급 함수
function getAuthToken() {
    const refreshPayload = JSON.stringify({refreshToken});
    const refreshHeaders = {'Content-Type': 'application/json'};

    const refreshRes = http.post(`${AUTH_URL}`, refreshPayload, {headers: refreshHeaders});
    check(refreshRes, {'액세스 토큰 재발급 성공': (r) => r.status === 200});

    const parsedBody = JSON.parse(refreshRes.body);
    return parsedBody.data.accessToken;
}

// 테스트 시나리오
export default function () {
    const token = getAuthToken();
    const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
    };

    const TEST_TYPE = __ENV.TEST_TYPE || 'list'; // 기본 테스트 타입 설정 (list)

    if (TEST_TYPE === 'create') {
        // 게시물 등록 테스트
        const response = http.post(
            `${BASE_URL}`,
            JSON.stringify({title: '테스트 게시물', contents: '테스트 내용'}),
            {headers}
        );
        check(response, {'게시물 등록 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'list') {
        // 게시물 목록 조회 테스트
        const response = http.post(
            `${BASE_URL}/list`,
            JSON.stringify({page: 1, size: 10}),
            {headers}
        );
        check(response, {'게시물 목록 조회 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'detail') {
        // 게시물 상세 조회 테스트
        const boardId = 1; // 테스트할 게시물 ID
        const response = http.get(`${BASE_URL}?boardId=${boardId}`, {headers});
        check(response, {'상세 게시물 조회 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'update') {
        // 게시물 수정 테스트
        const boardId = 1; // 수정할 게시물 ID
        const response = http.put(
            `${BASE_URL}?boardId=${boardId}`,
            JSON.stringify({title: '수정된 게시물', contents: '수정된 내용'}),
            {headers}
        );
        check(response, {'게시물 수정 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'delete') {
        // 게시물 삭제 테스트
        const boardId = 1; // 삭제할 게시물 ID
        const response = http.del(`${BASE_URL}?boardId=${boardId}`, null, {headers});
        check(response, {'게시물 삭제 응답이 200': (r) => r.status === 200});
    }

    sleep(1);
}
