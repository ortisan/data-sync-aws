import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    stages: [
        {duration: '10s', target: 10, rps: 100},
        {duration: '10s', target: 10, rps: 1},
        {duration: '10s', target: 20, rps: 1},
        {duration: '1m', target: 20, rps: 1},
    ],
};

export default function () {

    var author_uuids = [
        "2b325354-f11d-4530-8b27-70c63988d095",
        "b70ac1cf-ff78-45e8-8017-520763065f68",
        "b989e6df-2704-4265-981f-3913babf134e"
    ];

    var idx = Math.floor(Math.random() * 2);

    var json = {
        "title": "Testing POG",
        "publish_date": "2021-01-01",
        "authors": [
            {
                "id": author_uuids[idx],
                "roles": ["author"]
            }
        ]

    }

    const payload = JSON.stringify(json);

    var params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let res = http.post("http://localhost:8080/posts", payload, params);
    // console.log(JSON.stringify(res))
    check(res, {'status was 200': (r) => r.status == 200});
    sleep(1);
}