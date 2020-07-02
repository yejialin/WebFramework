<html>
<head>
    <meta charset="utf-8">
    <title>Ajax TODO 程序</title>
</head>
<body>
<h1>TODO</h1>

<input id="id-input-add" style="border: solid" name="content" placeholder="请输入TODO" />
<br>
<button id="id-button-add" type="submit">添加</button>

<div id="id-div-todoList">

</div>


<script>
    var log = console.log.bind(console);

    var ajax = function (method, path, data, callback) {
        var r = new XMLHttpRequest()
        r.open(method, path, true)
        r.setRequestHeader("Content-Type", "application/json")

        r.onreadystatechange = function() {
            if (r.readyState === 4) {
                callback(r.response)
            }
        }

        data = JSON.stringify(data)
        r.send(data)
    }

    var todoTemlate = function (todo) {
        var t = `
            <div class="todo-cell">
                <span>${todo.id}: ${todo.content} </span>
            </div>
        `
        return t;
    }

    var insertTodo = function (todoHtml) {
        var divTodo = document.querySelector("#id-div-todoList");
        divTodo.insertAdjacentHTML("beforeEnd", todoHtml)
    }

    var bindEvents = function () {
        var buttonAdd = document.querySelector("#id-button-add")
        buttonAdd.addEventListener("click", function () {
            log("buttonAdd")

            var input = document.querySelector("#id-input-add")
            var content = input.value
            log("input: ", content)

            var path = "/ajax/todo/add"
            var method = "POST"
            var data = {
                content: content
            }

            ajax(method, path, data, function (response) {
                var todo = JSON.parse(response);
                console.log("ajax todo add: ", response)
                var todoHtml = todoTemlate(todo);
                insertTodo(todoHtml);
            })
        })
    }

    var loadTodos = function () {
        var path = "/ajax/todo/all"
        var method = "POST"
        var data = ""

        ajax(method, path, data, function (response) {
            log("response: ", response)
            var todoList = JSON.parse(response)
            log("todo all:", todoList)

            for(let i = 0; i < todoList.length; i++) {
                var todo = todoList[i];
                var todoHtml = todoTemlate(todo);
                insertTodo(todoHtml);
            }
        })
    }

    var _main = function () {
        bindEvents()
        loadTodos();
    }

    _main()

</script>

</body>
</body>
</html>