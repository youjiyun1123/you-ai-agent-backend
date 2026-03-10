package com.you.youaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 终止工具(作用是让自主规划智能体能够合理的中断)
 */
public class TerminateTool {
    @Tool(description = """
            Terminate the interaction when the request is met OR if the assistance cannot
            proceed further with the task.When you have to finish all the tasks,call this tool to end the task.
            """)
    public String doTerminate(){
        return "任务结束";
    }
}
