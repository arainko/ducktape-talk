$version: "2.0"

namespace seriousBusinessApi

use alloy#dateFormat
use alloy#UUID
use smithytranslate#contentType

service SeriousBusinessApiService {
    operations: [
        CreateConferenceOp
        CreateTalkOp
        DeleteConferenceOp
        DeleteTalkOp
        FetchConferencesOp
        UpdateConferenceOp
        UpdateTalkOp
    ]
}

@http(
    method: "POST"
    uri: "/conferences"
    code: 201
)
operation CreateConferenceOp {
    input: CreateConferenceOpInput
    output: CreateConferenceOp201
    errors: [
        CreateConferenceOp400
    ]
}

@http(
    method: "POST"
    uri: "/conferences/{conferenceId}/talks"
    code: 201
)
operation CreateTalkOp {
    input: CreateTalkOpInput
    output: CreateTalkOp201
    errors: [
        CreateTalkOp400
        CreateTalkOp404
    ]
}

@http(
    method: "DELETE"
    uri: "/conferences/{conferenceId}"
    code: 200
)
operation DeleteConferenceOp {
    input: DeleteConferenceOpInput
    output: Unit
    errors: [
        DeleteConferenceOp404
    ]
}

@http(
    method: "DELETE"
    uri: "/conferences/{conferenceId}/talks/{talkId}"
    code: 200
)
operation DeleteTalkOp {
    input: DeleteTalkOpInput
    output: Unit
    errors: [
        DeleteTalkOp404
    ]
}

@http(
    method: "GET"
    uri: "/conferences"
    code: 200
)
operation FetchConferencesOp {
    input: Unit
    output: FetchConferencesOp200
}

@http(
    method: "PUT"
    uri: "/conferences/{conferenceId}"
    code: 200
)
operation UpdateConferenceOp {
    input: UpdateConferenceOpInput
    output: Unit
    errors: [
        UpdateConferenceOp400
        UpdateConferenceOp404
    ]
}

@http(
    method: "PUT"
    uri: "/conferences/{conferenceId}/talks/{talkId}"
    code: 201
)
operation UpdateTalkOp {
    input: UpdateTalkOpInput
    output: UpdateTalkOp201
    errors: [
        UpdateTalkOp400
        UpdateTalkOp404
    ]
}

structure CreateConference {
    @required
    name: String
    @required
    dateSpan: DateSpan
    @required
    city: String
}

structure CreateConferenceOp201 {
    @httpPayload
    @required
    @contentType("application/json")
    body: CreatedId
}

@error("client")
@httpError(400)
structure CreateConferenceOp400 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ValidationErrors
}

structure CreateConferenceOpInput {
    @httpPayload
    @required
    @contentType("application/json")
    body: CreateConference
}

structure CreatedId {
    @required
    createdId: UUID
}

structure CreateTalk {
    @required
    name: String
    @required
    elevatorPitch: String
    @required
    presenter: Presenter
}

structure CreateTalkOp201 {
    @httpPayload
    @required
    @contentType("application/json")
    body: CreatedId
}

@error("client")
@httpError(400)
structure CreateTalkOp400 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ValidationErrors
}

@error("client")
@httpError(404)
structure CreateTalkOp404 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ErrorMessage
}

structure CreateTalkOpInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpPayload
    @required
    @contentType("application/json")
    body: CreateTalk
}

structure DateSpan {
    @dateFormat
    @required
    start: String
    @dateFormat
    @required
    end: String
}

@error("client")
@httpError(404)
structure DeleteConferenceOp404 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ErrorMessage
}

structure DeleteConferenceOpInput {
    @httpLabel
    @required
    conferenceId: UUID
}

@error("client")
@httpError(404)
structure DeleteTalkOp404 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ErrorMessage
}

structure DeleteTalkOpInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpLabel
    @required
    talkId: UUID
}

structure ErrorMessage {
    @required
    errorMessage: String
}

structure FetchConferencesOp200 {
    @httpPayload
    @required
    @contentType("application/json")
    body: Body
}

structure GetConference {
    @required
    id: UUID
    @required
    name: String
    @required
    dateSpan: DateSpan
    @required
    city: String
    @required
    talks: Talks
}

structure GetTalk {
    @required
    id: UUID
    @required
    name: String
    @required
    elevatorPitch: String
    @required
    presenter: Presenter
}

structure Presenter {
    @required
    name: String
    @required
    bio: String
    pronouns: Pronouns
}

structure UpdateConference {
    @required
    name: String
    @required
    dateSpan: DateSpan
    @required
    city: String
}

@error("client")
@httpError(400)
structure UpdateConferenceOp400 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ValidationErrors
}

@error("client")
@httpError(404)
structure UpdateConferenceOp404 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ErrorMessage
}

structure UpdateConferenceOpInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpPayload
    @required
    @contentType("application/json")
    body: UpdateConference
}

structure UpdateTalk {
    @required
    name: String
    @required
    elevatorPitch: String
    @required
    presenter: Presenter
}

structure UpdateTalkOp201 {
    @httpPayload
    @required
    @contentType("application/json")
    body: CreatedId
}

@error("client")
@httpError(400)
structure UpdateTalkOp400 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ValidationErrors
}

@error("client")
@httpError(404)
structure UpdateTalkOp404 {
    @httpPayload
    @required
    @contentType("application/json")
    body: ErrorMessage
}

structure UpdateTalkOpInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpLabel
    @required
    talkId: UUID
    @httpPayload
    @required
    @contentType("application/json")
    body: UpdateTalk
}

structure ValidationErrors {
    @required
    errors: Errors
}

list Body {
    member: GetConference
}

list Errors {
    member: String
}

list Talks {
    member: GetTalk
}

enum Pronouns {
    theythem = "they/them"
    sheher = "she/her"
    hehim = "he/him"
}
