package dev.myleshenp.contentnotification.constants;

public class ApplicationConstants {

    public static int CONTENT_SIZE_FOR_NOTIFICATIONS = 1;

    public static String EMAIL_TEMPLATE =
            """
                    <html>
                    <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                        content="width=device-width, user-scalable=no,
                        initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                    <meta http-equiv="X-UA-Compatible" content="ie=edge">
                    <title>Document</title>
                    </head>

                    <body style="display: flex; flex-direction: column">
                        <h1>Content Of the Day</h1>
                        <h2>Type: ${type}</h2>
                        <blockquote>
                            ${quote}
                        </blockquote>
                        <i style="display: flex;justify-content: end">---${author}</i>
                    </body>

                    </html>
                    """;

    public static String MESSAGE_TEMPLATE =
            """
                    Content Of the Day

                    Type: ${type}

                    ${quote}

                            --- ${author}
                    """;
}
