$version: "2.0"

metadata smithy4sErrorsAsScala3Unions = true
namespace io.github.arainko.test

@trait()
map extensions {
    key: String
    value: String
}

structure Person {
    @required
    name: String

    age: Age

    @required
    peers: Peers

    @extensions(
        "format": "int64",
        "format2": "int64",
    )
    test: Integer
}

list Peers {
    member: String
}

integer Age 
