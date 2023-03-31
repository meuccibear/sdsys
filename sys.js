function send(data) {
    if (window._connection) {
        window._connection.send(JSON.stringify(data))
    } else {
        window._connection = new WebSocket('ws://192.168.0.29:8889');
    }
}



window.option.send(
    {
        "did": "test123",
        "action": "login",
        "req": {
            "dname": "",
            "daddr": ""
        },
        "req_event": 1,
        "seq_id": 1567087026
    }
)

window.option.send({
    "action": "heartbeat",
    "did": "test123",
    "req": {
        "key": "4dafeb69dc1f98bda"
    },
    "req_event": 1,
    "seq_id": 1567087026
},true)


window.option = {
    "sw":true,
    "timeout": 1000 * 30,
    "send": function (data, b) {
        window.option._connection.send(JSON.stringify(data))
        if(b){
            setTimeout(function(){
                window.option.send(data, window.option.sw)
            }, window.option.timeout)
        }
    },
    "connection":function () {
        window.option._connection = new WebSocket('ws://192.168.0.29:8889');
        setTimeout(function(){
            window.option.send(
                {
                    "did": "test123",
                    "action": "login",
                    "req": {
                        "dname": "",
                        "daddr": ""
                    },
                    "req_event": 1,
                    "seq_id": 1567087026
                }
            )
            setTimeout(function (){
                window.option.send({
                    "action": "heartbeat",
                    "did": "test123",
                    "req": {
                        "key": "4dafeb69dc1f98bda"
                    },
                    "req_event": 1,
                    "seq_id": 1567087026
                },true)
            }, 500)
        }, 500)
    }
}
window.option.connection()





