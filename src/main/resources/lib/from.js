function fromBool(b){
    if(b)
        return [null, null]
    return []
}
function fromInt(n){
    if(n === 0)
        return null
    return [
        null,
        fromInt(n-1)
    ]
}
function fromString(str){
    if(str.length === 1)
        return str
    return [
        str[0],
        fromString(str.substring(1))
    ]
}