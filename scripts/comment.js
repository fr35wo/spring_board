import http from 'k6/http';
import {check, sleep} from 'k6';

// TEST_TYPE=create k6 run --out influxdb=http://localhost:8086 comment.js

export const options = {
    stages: [
        {duration: '30s', target: 1000}, // 30초 동안 1000명의 VU로 증가
        {duration: '1m', target: 5000},  // 1분 동안 5000명의 VU 유지
        {duration: '30s', target: 0},    // 30초 동안 점진적으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% 요청이 500ms 이내 완료
    },
};

const BASE_URL = 'http://localhost:8091/api/comment'; // 댓글 API의 기본 URL
const AUTH_URL = 'http://localhost:8091/api/auth/access'; // 인증 API URL
const USERNAME = 'string1';
const PASSWORD = 'string';
const refreshToken = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmcxIiwiZXhwIjoxNzM3MTYyNDk5LCJyYW5kb20iOiI2ZGRjYjFiMy01NGE5LTQzMTctOTg5Ni1jYTFmNTg3YmNiMWIifQ.jAZI8rSz0MDa8CuFXmQjipsQBsjWizIfFmeogXQb6vgAPeXAlV7t9YkXgwQCHhwQzFxIC1vhV_igzhrj5LlqRg'; // 리프레시 토큰

// JWT 토큰 발급 함수
function getAuthToken() {
    const refreshPayload = JSON.stringify({refreshToken});
    const refreshHeaders = {'Content-Type': 'application/json'};

    const refreshRes = http.post(AUTH_URL, refreshPayload, {headers: refreshHeaders});
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

    const BOARD_ID = 1; // 테스트할 게시물 ID
    const TEST_TYPE = __ENV.TEST_TYPE || 'list'; // 기본 테스트 타입 설정 (list)

    if (TEST_TYPE === 'create') {
        // 댓글 등록 테스트
        const response = http.post(
            `${BASE_URL}/${BOARD_ID}`,
            JSON.stringify({content: '테스트 댓글'}),
            {headers}
        );
        check(response, {'댓글 등록 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'list') {
        // 댓글 목록 조회 테스트
        const response = http.post(
            `${BASE_URL}/${BOARD_ID}/comments`,
            JSON.stringify({page: 1, size: 10}),
            {headers}
        );
        check(response, {'댓글 목록 조회 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'update') {
        // 댓글 수정 테스트
        const COMMENT_ID = 1; // 테스트할 댓글 ID
        const response = http.put(
            `${BASE_URL}/${COMMENT_ID}`,
            JSON.stringify({content: '수정된 테스트 댓글'}),
            {headers}
        );
        check(response, {'댓글 수정 응답이 200': (r) => r.status === 200});
    } else if (TEST_TYPE === 'delete') {
        // 댓글 삭제 테스트
        const COMMENT_ID = 1; // 테스트할 댓글 ID
        const response = http.del(`${BASE_URL}/${COMMENT_ID}`, null, {headers});
        check(response, {'댓글 삭제 응답이 200': (r) => r.status === 200});
    }

    sleep(1); // 1초 대기
}
