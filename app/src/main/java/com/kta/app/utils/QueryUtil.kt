package com.kta.app.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object QueryUtil {
    fun nearestQuery(type: QueryType): SimpleSQLiteQuery {
        var query = ""
        when (type) {
            QueryType.CURRENT_DAY -> query = """
                 SELECT * FROM ScheduleEntity 
                 WHERE day = (strftime('%w', 'now') + 1)
                 AND strftime('%H:%M', startTime) > strftime('%H:%M', 'now')
                 ORDER BY strftime('%H:%M', startTime) ASC LIMIT 1
                 """

            QueryType.NEXT_DAY -> query = """
                 SELECT * FROM ScheduleEntity 
                 WHERE day > (strftime('%w', 'now') + 1)
                 ORDER BY day,strftime('%H:%M', startTime) ASC LIMIT 1
                 """

            QueryType.PAST_DAY -> query = """
                 SELECT * FROM ScheduleEntity 
                 WHERE day >= 0
                 ORDER BY day, strftime('%H:%M', startTime) ASC LIMIT 1
                 """
        }

        return SimpleSQLiteQuery(query)
    }
}

enum class QueryType {
    CURRENT_DAY,
    NEXT_DAY,
    PAST_DAY
}
