package fr.o80.slobs.ws

internal fun prepareAuthRequest(
    token: String
) : String {
    return """["{\"jsonrpc\": \"2.0\", \"id\": \"#ID#\", \"method\": \"auth\", \"params\": {\"resource\": \"TcpServerService\", \"args\": [\"$token\"] }}"]"""
}