package com.piveguyz.empickbackend.common.constants;

public class ApiExamples {
    private ApiExamples() {}

    public static final String ERROR_400_EXAMPLE =
            """
            {
              "success": false,
              "code": 400,
              "message": "잘못된 요청입니다.",
              "data": null
            }
            """;

    public static final String ERROR_401_EXAMPLE =
            """
            {
              "success": false,
              "code": 401,
              "message": "인증이 필요합니다.",
            """;
    public static final String ERROR_404_EXAMPLE =
            """
            {
              "success": false,
              "code": 404,
            """;
    public static final String ERROR_500_EXAMPLE =
            """
            {
              "success": false,
              "code": 500,
              "message": "서버 오류입니다.",
              "data": null
            }
            """;
    public static final String ERROR_503_EXAMPLE =
            """
            {
              "success": false,
              "code": 503,
              "message": "서버가 점검중입니다.",
            """;

    public static final String ERROR_503_EXAMPLE_WITH_DATA =
            """
            {
              "success": false,
              "code": 503,
            """;

    public static final String ERROR_404_EXAMPLE_WITH_DATA =
            """
            {
              "success": false,
              "code": 404,
              "data": null
            }
            """;
}
