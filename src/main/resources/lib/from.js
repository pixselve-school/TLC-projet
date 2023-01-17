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
        return [null, null]
    return null
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
    if(str.length === 0)
        return null
    return [
        str[0],
        fromString(str.substring(1))
    ]
}