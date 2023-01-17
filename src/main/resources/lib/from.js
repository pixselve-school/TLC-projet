/**
 * Transform a text into a tree
 * @param any string, int or boolean
 * @returns a tree
 */
function from(any){
    let n = parseInt(any)
    if(!isNaN(n))
        return fromInt(n)
    if(any === "true")
        return fromBool(true)
    if(any === "false")
        return fromBool(false)
    return fromString(any)
}
function fromBool(b){
    if(b)
        return [null, []]
    return []
}
function fromInt(n){
    if(n === 0)
        return []
    return [
        null,
        fromInt(n-1)
    ]
}
function fromString(str){
    if(str.length === 0)
        return []
    return [
        str[0],
        fromString(str.substring(1))
    ]
}