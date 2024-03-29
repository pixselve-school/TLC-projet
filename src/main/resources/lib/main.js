/**
 * Launch the program if the number of parameters in
 * the console matches the number of arguments in the main function
 */
(()=>{
    let args = process.argv.slice(2).map(n => from(n))

    let n = args.length

    if (n !== NUMBER_OF_MAIN_ARGS) {
        console.error("Expected " + NUMBER_OF_MAIN_ARGS + " arguments, found " + n)
    } else {
        let res = tlc_main(args)

        for(let r of res) {
            print(r)
        }
    }
})()
