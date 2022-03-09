import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    stages: [
        {duration: '10s', target: 10, rps: 1},
        {duration: '10s', target: 10, rps: 1},
        {duration: '10s', target: 20, rps: 1},
        {duration: '1m', target: 20, rps: 1},
    ],
};

export default function () {

    const payload = JSON.stringify({
        user_id: "f65d0fbd-b428-4bf1-9a83-ced17dad6bc8",
        title: "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        body: "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
    });

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