Data file format:

Type: 
0: Book
1: Article
--------------------------
#DOCUMENTS
#BOOK
bookId
bookTitle
author,author
comment // only one line
edition
publishedYear
ISBN
chapterId#chapterTitle\tchapterId#tchapterTitle
createdTime
lastUpdatedTime
#ARTICLE
articleId
articleTitle
author,author
comment // only one line
source
createdTime
lastUpdateTime
#END_DOCUMENTS
#TAGS
tagId,tagText // only word and white space
#END_TAGS
#NOTES
#BOOK
noteId
bookId
chapterId
tagId,tagId
text // multiple lines
#END_TEXT
createdTime // timestamp
#ARTICLE
noteId
articleId
tagId,tagId
text // multiple lines
#END_TEXT
createdTime // timestamp
#END_NOTES
--------------------------
<DOCUMENTS>
    <BOOKS>
        <BOOK>
            <DOCUMENT_ID />
            <DOCUMENT_TITLE />
            <AUTHORS />
            <COMMENT />
            <EDITION />
            <PUBLISHED_YEAR />
            <ISBN />
            <CHAPTERS>
                <CHAPTER_ID />
                <CHAPTER_TITLE />
            <CHAPTERS />
            <CREATED_TIME />
            <LAST_UPDATE_TIME />
        <BOOK />
    <BOOKS/>
    <ARTICLES>
        <ARTICLE>
            <DOCUMENT_ID />
                <DOCUMENT_TITLE />
                <AUTHORS />
                <COMMENT />
                <SOURCE />
                <CREATED_TIME />
                <LAST_UPDATE_TIME />
        <ARTICLE />
    <ARTICLES />
    <TAGS>
        <TAG>
            <TAG_ID />
            <TAG_TEXT />
        <TAG />
    <TAGS />
    <NOTES>
        <BOOK_NOTES>
            <BOOK_NOTE>
                <NOTE_ID />
                <BOOK_ID />
                <CHAPTER_ID />
                <TAG_LIST>
                    <TAG />
                <TAG_LIST />
                <NOTE_TEXT />
                <CREATED_TIME />
            <BOOK_NOTE />
        <BOOK_NOTES />
        <ARTICLE_NOTES>
            <ARTICLE_NOTE>
                <NOTE_ID />
                <ARTICLE_ID />
                <TAG_LIST>
                    <TAG />
                <TAG_LIST />
                <NOTE_TEXT />
                <CREATED_TIME />
            <ARTICLE_NOTE />
        <ARTICLE_NOTES />
    <NOTES />
<DOCUMENTS/>
