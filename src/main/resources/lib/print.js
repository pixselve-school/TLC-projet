function print(t){

    if(Array.isArray(t) && t.length >= 1){
        let type = t[0]
        if(type === "int")
            console.log(toInt(t[1]))
        else if(type === "bool")
            console.log(toBool(t[1]))
        else if(type === "string")
            console.log(toString(t[1]))
        else
            printTree(t, "", true)
    }else{
        printTree(t, "", true)
    }

    // console.log(t)
}
function printTree(t, tab = "", last = false){
    let tabc = tab + ((last) ? "└─" : "├─")

    if(t === null || t === undefined)
        console.log(tabc + "Nil")
    else if(Array.isArray(t)){
        console.log(tabc + "Node")

        for(let i in t) {
            if(last)
                printTree(t[i], tab + "  ", i == t.length-1) // Do not change the ==
            else
                printTree(t[i], tab + "│ ", i == t.length-1) // Do not change the ==
        }
    }else
        console.log(tab + t)
}