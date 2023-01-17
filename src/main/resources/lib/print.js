function print(t){
    console.log(toInt(t))
    console.log(toBool(t))
    console.log(toString(t))
    printTree(t, "", true)

    console.log(t)
}
function printTree(t, tab = "", last = false){
    let tabc = tab + ((last) ? "└─" : "├─")

    if(t === null || t === undefined)
        console.log(tabc + "Nil")
    else if(Array.isArray(t)){
        console.log(tabc + "Node")

        for(let i in t) {
            if(last)
                printTree(t[i], tab + "  ", i == t.length-1)
            else
                printTree(t[i], tab + "│ ", i == t.length-1)
        }
    }else
        console.log(tab + t)
}