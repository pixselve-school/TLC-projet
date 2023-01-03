/**
 * Launch the program if the number of parameters in
 * the console matches the number of arguments in the main function
 */
(()=>{
    let args = process.argv.slice(2)

    let n = args.length

    if (n !== NUMBER_OF_MAIN_ARGS) {
        console.error("Expected " + NUMBER_OF_MAIN_ARGS + " arguments, found " + n)
    } else {
        tlc_main(process.argv)
    }
})()

/**
 * Checks if the given value is true
 * @param a the array to check
 *
 * @return true if the value is true, false otherwise
 *
 * if a is nil, then false is returned
 * else true is returned
 */
function toBool(a) {
    if (a === null) {
        return false;
    }
    if (a === undefined) {
        return false;
    }
    return a.length === 2;

}

/**
 * Convert an array to the corresponding number
 * @param a the array to convert
 * @returns {number} the number
 */
function toInt(a) {
    if (a === null || a === undefined) {
        return 0;
    }
    if (a.length === 1) {
        return 0;
    }
    return toInt(a[1]) + 1;
}

