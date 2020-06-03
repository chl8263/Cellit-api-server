
// About HTTP protocol
class HTTP{
    // http method
    static get METHOD_GET(){ return "GET" }
    static get METHOD_POST(){ return "POST" }
    static get METHOD_PUT(){ return "PUT" }
    static get METHOD_DELETE(){ return "DELETE" }

    static get BASIC_TOKEN_PREFIX(){ return "Bearer " }
}

class MediaType{
    static get JSON (){ return "application/json" }
    static get HTML (){ return "text/html" }
}

// jwt
let JWT_TOKEN = "";