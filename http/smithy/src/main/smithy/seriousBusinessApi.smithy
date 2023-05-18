$version: "2.0"

metadata smithy4sErrorsAsScala3Unions = true
namespace io.github.arainko.talk.API

use alloy#UUID
use alloy#dateFormat
use alloy#simpleRestJson
use smithy4s.meta#refinement

apply dateFormat @refinement(targetType: "java.time.LocalDate", providerImport: "io.github.arainko.talk.given")

@simpleRestJson
service SeriousBusinessApiService {
    operations: [
        CreateConference
        CreateTalk
        DeleteConference
        DeleteTalk
        FetchConferences
        UpdateConference
        UpdateTalk
    ]
}

@http(method: "POST", uri: "/conferences", code: 201)
operation CreateConference {
    input: CreateConferenceInput
    output: CreateConferenceOutput
    errors: [ValidationErrors]
}

@http(method: "POST", uri: "/conferences/{conferenceId}/talks", code: 201)
operation CreateTalk {
    input: CreateTalkInput
    output: CreateTalkOutput
    errors: [ValidationErrors, ConferenceNotFound]
}

@http(method: "DELETE", uri: "/conferences/{conferenceId}", code: 200)
operation DeleteConference {
    input: DeleteConferenceInput
    output: Unit
    errors: [ConferenceNotFound]
}

@http(method: "DELETE", uri: "/conferences/{conferenceId}/talks/{talkId}", code: 200)
operation DeleteTalk {
    input: DeleteTalkInput
    output: Unit
    errors: [ConferenceOrTalkNotFound]
}

@http(method: "GET", uri: "/conferences", code: 200)
operation FetchConferences {
    input: Unit
    output: FetchConferencesOutput
}

@http(method: "PUT", uri: "/conferences/{conferenceId}", code: 200)
operation UpdateConference {
    input: UpdateConferenceInput
    output: Unit
    errors: [ValidationErrors, ConferenceNotFound]
}

@http(method: "PUT", uri: "/conferences/{conferenceId}/talks/{talkId}", code: 201)
operation UpdateTalk {
    input: UpdateTalkInput
    output: UpdateTalkOutput
    errors: [ValidationErrors, ConferenceOrTalkNotFound]
}

structure CreateConferenceOutput {
    @httpPayload
    @required
    body: CreatedId
}

structure CreateConferenceInput {
    @required
    body: CreateConferenceBody
}

structure CreatedId {
    @required
    createdId: UUID
}

structure CreateTalkBody {
    @required
    name: String
    @required
    elevatorPitch: String
    @required
    presenter: Presenter
}

structure CreateTalkOutput {
    @httpPayload
    @required
    body: CreatedId
}

@error("client")
@httpError(404)
structure ConferenceNotFound {
    @httpPayload
    @required
    body: String
}

@error("client")
@httpError(404)
structure ConferenceOrTalkNotFound {
    @httpPayload
    @required
    body: String
}

structure CreateTalkInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpPayload
    @required
    body: CreateTalkBody
}

structure DateSpan {
    @dateFormat
    @required
    start: String
    @dateFormat
    @required
    end: String
}

structure DeleteConferenceInput {
    @httpLabel
    @required
    conferenceId: UUID
}

structure DeleteTalkInput {
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

structure FetchConferencesOutput {
    @httpPayload
    @required
    body: Conferences
}

structure CreateConferenceBody {
    @required
    name: String
    @required
    dateSpan: DateSpan
    @required
    city: String
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

structure UpdateConferenceBody {
    @required
    name: String
    @required
    dateSpan: DateSpan
    @required
    city: String
}

structure UpdateConferenceInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpPayload
    @required
    body: UpdateConferenceBody
}

structure UpdateTalkBody {
    @required
    name: String
    @required
    elevatorPitch: String
    @required
    presenter: Presenter
}

structure UpdateTalkOutput {
    @httpPayload
    @required
    body: CreatedId
}

structure UpdateTalkInput {
    @httpLabel
    @required
    conferenceId: UUID
    @httpLabel
    @required
    talkId: UUID
    @httpPayload
    @required
    body: UpdateTalkBody
}

@error("client")
@httpError(400)
structure ValidationErrors {
    @required
    errors: Errors
}

list Conferences {
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
