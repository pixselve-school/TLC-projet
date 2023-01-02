let args = process.argv.slice(2)

let n = args.length

if(n != NUMBER_OF_MAIN_ARGS){
    console.error("Expected " + NUMBER_OF_MAIN_ARGS + " arguments, found " + n)
}else{
    tlc_main(process.argv)
}

