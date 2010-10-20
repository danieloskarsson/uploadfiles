<%@ page import="se.knowit.developer.files.*" %>
<html>
    <head>
        <title>Know IT Files</title>
    </head>
    <body>
        <h2>Upload file</h2>
        <form method="post" enctype="multipart/form-data" action="/files/upload">
        File to upload: <input type="file" name="filepath">
        <input type="submit" value="Upload">
        </form>
    </body>
</html>
