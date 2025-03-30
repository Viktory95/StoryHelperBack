CREATE TABLE IF NOT EXISTS story_helper.st_view (
            id uuid PRIMARY KEY,
            name text
            );
CREATE TABLE IF NOT EXISTS story_helper.style (
            id uuid PRIMARY KEY,
            book text,
            author text
            );
            CREATE TABLE IF NOT EXISTS story_helper.story (
            id uuid PRIMARY KEY,
            name text,
            genres list<uuid>,
            characters list<uuid>,
            nodes list<uuid>,
            style uuid,
            stView uuid,
            stUser text,
            isPublic boolean
            );
            CREATE TABLE IF NOT EXISTS story_helper.node (
            id uuid PRIMARY KEY,
            name text,
            textt text,
            flags list<uuid>,
            description text,
            nPrev list<uuid>,
            nNext list<uuid>
            );
            CREATE TABLE IF NOT EXISTS story_helper.log (
            id uuid PRIMARY KEY,
            stAction text,
            stUser text,
            lDate text,
            tableName text,
            objectId uuid
            );
            CREATE TABLE IF NOT EXISTS story_helper.genre (
            id uuid PRIMARY KEY,
            name text,
            description text
            );
            CREATE TABLE IF NOT EXISTS story_helper.flag (
            id uuid PRIMARY KEY,
            icon text,
            placeholder text
            );
            CREATE TABLE IF NOT EXISTS story_helper.character (
            id uuid PRIMARY KEY,
            name text,
            gender text,
            birthday text,
            appearance text,
            features text,
            characterDescription text,
            importanceRate int
            );