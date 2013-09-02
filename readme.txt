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
