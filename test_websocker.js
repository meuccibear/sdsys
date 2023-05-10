window.option = {
    "url_dev": "ws://127.0.0.1:8889",
    "url_prod": "ws://165.154.134.182:8889",
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
        window.option._connection = new WebSocket(window.option.url_dev);
        setTimeout(function(){
            window.option.send(
                {
                    "did": "test1",
                    "action": "login",
                    "req": {
                        "dname": "test1",
                        "daddr": "test1"
                    },
                    "req_event": 1,
                    "seq_id": 1567087026
                }
            )
            setTimeout(function (){
                window.option.send({
                    "action": "heartbeat",
                    "did": "test1",
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





