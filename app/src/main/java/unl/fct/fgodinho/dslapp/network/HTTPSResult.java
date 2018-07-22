package unl.fct.fgodinho.dslapp.network;

public class HTTPSResult {

    // TODO: refactor this into a json result that is able to parse SH replies

    private int statusCode;
    private String content, contentType;

    public HTTPSResult(int statusCode, String content, String contentType) {
        this.statusCode = statusCode;
        this.content = content;
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

}
