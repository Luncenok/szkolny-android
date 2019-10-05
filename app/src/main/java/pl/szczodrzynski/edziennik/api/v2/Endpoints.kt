/*
 * Copyright (c) Kuba Szczodrzyński 2019-9-20.
 */

package pl.szczodrzynski.edziennik.api.v2

import pl.szczodrzynski.edziennik.api.v2.models.Feature

const val ENDPOINT_LIBRUS_API_ME                        = 1001
const val ENDPOINT_LIBRUS_API_SCHOOLS                   = 1002
const val ENDPOINT_LIBRUS_API_CLASSES                   = 1003
const val ENDPOINT_LIBRUS_API_VIRTUAL_CLASSES           = 1004
const val ENDPOINT_LIBRUS_API_UNITS                     = 1005
const val ENDPOINT_LIBRUS_API_USERS                     = 1006
const val ENDPOINT_LIBRUS_API_SUBJECTS                  = 1007
const val ENDPOINT_LIBRUS_API_CLASSROOMS                = 1008
const val ENDPOINT_LIBRUS_API_TIMETABLES                = 1015
const val ENDPOINT_LIBRUS_API_SUBSTITUTIONS             = 1016
const val ENDPOINT_LIBRUS_API_NORMAL_GC                 = 1021
const val ENDPOINT_LIBRUS_API_POINT_GC                  = 1022
const val ENDPOINT_LIBRUS_API_DESCRIPTIVE_GC            = 1023
const val ENDPOINT_LIBRUS_API_TEXT_GC                   = 1024
const val ENDPOINT_LIBRUS_API_DESCRIPTIVE_TEXT_GC       = 1025
const val ENDPOINT_LIBRUS_API_BEHAVIOUR_GC              = 1026
const val ENDPOINT_LIBRUS_API_NORMAL_GRADES             = 1031
const val ENDPOINT_LIBRUS_API_POINT_GRADES              = 1032
const val ENDPOINT_LIBRUS_API_DESCRIPTIVE_GRADES        = 1033
const val ENDPOINT_LIBRUS_API_TEXT_GRADES               = 1034
const val ENDPOINT_LIBRUS_API_DESCRIPTIVE_TEXT_GRADES   = 1035
const val ENDPOINT_LIBRUS_API_BEHAVIOUR_GRADES          = 1036
const val ENDPOINT_LIBRUS_API_EVENTS                    = 1040
const val ENDPOINT_LIBRUS_API_EVENT_TYPES               = 1041
const val ENDPOINT_LIBRUS_API_HOMEWORK                  = 1050
const val ENDPOINT_LIBRUS_API_LUCKY_NUMBER              = 1060
const val ENDPOINT_LIBRUS_API_NOTICES                   = 1070
const val ENDPOINT_LIBRUS_API_ATTENDANCE_TYPES          = 1080
const val ENDPOINT_LIBRUS_API_ATTENDANCE                = 1081
const val ENDPOINT_LIBRUS_API_ANNOUNCEMENTS             = 1090
const val ENDPOINT_LIBRUS_API_PT_MEETINGS               = 1100
const val ENDPOINT_LIBRUS_API_TEACHER_FREE_DAYS         = 1110
const val ENDPOINT_LIBRUS_API_SCHOOL_FREE_DAYS          = 1120
const val ENDPOINT_LIBRUS_API_CLASS_FREE_DAYS           = 1130
const val ENDPOINT_LIBRUS_SYNERGIA_INFO                 = 2010
const val ENDPOINT_LIBRUS_SYNERGIA_GRADES               = 2020
const val ENDPOINT_LIBRUS_MESSAGES_RECEIVED             = 3010
const val ENDPOINT_LIBRUS_MESSAGES_SENT                 = 3020
const val ENDPOINT_LIBRUS_MESSAGES_TRASH                = 3030
const val ENDPOINT_LIBRUS_MESSAGES_RECEIVERS            = 3040
const val ENDPOINT_LIBRUS_MESSAGES_GET                  = 3040

const val ENDPOINT_TEMPLATE_WEB_SAMPLE                  = 9991
const val ENDPOINT_TEMPLATE_WEB_SAMPLE_2                = 9992
const val ENDPOINT_TEMPLATE_API_SAMPLE                  = 9993

val endpoints = listOf(

        // LIBRUS: API
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_TIMETABLE, listOf(
                ENDPOINT_LIBRUS_API_TIMETABLES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_SUBSTITUTIONS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_AGENDA, listOf(
                ENDPOINT_LIBRUS_API_EVENTS to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_EVENT_TYPES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_PT_MEETINGS to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_TEACHER_FREE_DAYS to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_SCHOOL_FREE_DAYS to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_CLASS_FREE_DAYS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_GRADES, listOf(
                ENDPOINT_LIBRUS_API_NORMAL_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_POINT_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_DESCRIPTIVE_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_TEXT_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_DESCRIPTIVE_TEXT_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_BEHAVIOUR_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_NORMAL_GRADES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_POINT_GRADES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_DESCRIPTIVE_GRADES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_TEXT_GRADES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_DESCRIPTIVE_TEXT_GRADES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_BEHAVIOUR_GRADES to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_HOMEWORK, listOf(
                ENDPOINT_LIBRUS_API_HOMEWORK to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_BEHAVIOUR, listOf(
                ENDPOINT_LIBRUS_API_NOTICES to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_ATTENDANCE, listOf(
                ENDPOINT_LIBRUS_API_ATTENDANCE to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_ATTENDANCE_TYPES to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_ANNOUNCEMENTS, listOf(
                ENDPOINT_LIBRUS_API_ANNOUNCEMENTS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_STUDENT_INFO, listOf(
                ENDPOINT_LIBRUS_API_ME to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_SCHOOL_INFO, listOf(
                ENDPOINT_LIBRUS_API_SCHOOLS to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_UNITS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_CLASS_INFO, listOf(
                ENDPOINT_LIBRUS_API_CLASSES to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_TEAM_INFO, listOf(
                ENDPOINT_LIBRUS_API_VIRTUAL_CLASSES to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_LUCKY_NUMBER, listOf(
                ENDPOINT_LIBRUS_API_LUCKY_NUMBER to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_TEACHERS, listOf(
                ENDPOINT_LIBRUS_API_USERS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_SUBJECTS, listOf(
                ENDPOINT_LIBRUS_API_SUBJECTS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_CLASSROOMS, listOf(
                ENDPOINT_LIBRUS_API_CLASSROOMS to LOGIN_METHOD_LIBRUS_API
        ), listOf(LOGIN_METHOD_LIBRUS_API)),

        Feature(LOGIN_TYPE_LIBRUS, FEATURE_STUDENT_INFO, listOf(
                ENDPOINT_LIBRUS_SYNERGIA_INFO to LOGIN_METHOD_LIBRUS_SYNERGIA
        ), listOf(LOGIN_METHOD_LIBRUS_SYNERGIA)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_STUDENT_NUMBER, listOf(
                ENDPOINT_LIBRUS_SYNERGIA_INFO to LOGIN_METHOD_LIBRUS_SYNERGIA
        ), listOf(LOGIN_METHOD_LIBRUS_SYNERGIA)),
        /*Endpoint(LOGIN_TYPE_LIBRUS, FEATURE_GRADES, listOf(
                ENDPOINT_LIBRUS_SYNERGIA_GRADES to LOGIN_METHOD_LIBRUS_SYNERGIA
        ), listOf(LOGIN_METHOD_LIBRUS_SYNERGIA)),*/


        Feature(LOGIN_TYPE_LIBRUS, FEATURE_GRADES, listOf(
                ENDPOINT_LIBRUS_API_NORMAL_GC to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_API_NORMAL_GRADES to LOGIN_METHOD_LIBRUS_API,
                ENDPOINT_LIBRUS_SYNERGIA_GRADES to LOGIN_METHOD_LIBRUS_SYNERGIA
        ), listOf(LOGIN_METHOD_LIBRUS_API, LOGIN_METHOD_LIBRUS_SYNERGIA)),

        Feature(LOGIN_TYPE_LIBRUS, FEATURE_MESSAGES_INBOX, listOf(
                ENDPOINT_LIBRUS_MESSAGES_RECEIVED to LOGIN_METHOD_LIBRUS_MESSAGES
        ), listOf(LOGIN_METHOD_LIBRUS_MESSAGES)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_MESSAGES_SENT, listOf(
                ENDPOINT_LIBRUS_MESSAGES_SENT to LOGIN_METHOD_LIBRUS_MESSAGES
        ), listOf(LOGIN_METHOD_LIBRUS_MESSAGES))
)

val templateEndpoints = listOf(
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_STUDENT_INFO, listOf(
                ENDPOINT_TEMPLATE_WEB_SAMPLE to LOGIN_METHOD_TEMPLATE_WEB
        ), listOf(LOGIN_METHOD_TEMPLATE_WEB)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_SCHOOL_INFO, listOf(
                ENDPOINT_TEMPLATE_WEB_SAMPLE_2 to LOGIN_METHOD_TEMPLATE_WEB
        ), listOf(LOGIN_METHOD_TEMPLATE_WEB)),
        Feature(LOGIN_TYPE_LIBRUS, FEATURE_GRADES, listOf(
                ENDPOINT_TEMPLATE_API_SAMPLE to LOGIN_METHOD_TEMPLATE_API
        ), listOf(LOGIN_METHOD_TEMPLATE_API))
)