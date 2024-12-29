import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../components/ui/card.tsx";
import { Button } from "../components/ui/button";
import { FilePenLine, X } from "lucide-react";
import { EisenhowerMatrix } from "./EisenhowerMatrix.tsx";

type Task = {
  id: number;
  content: string;
  importance: "high" | "low";
  urgency: "urgent" | "not urgent";
};

type ColumnProps = {
  column: { id: number; title: string; tasks: Task[] };
  removeColumn: (id: number) => void;
  removeTask: (id1: number, id2: number) => void;
  getTaskColor: (importance: string, urgency: string) => string;
};

export const Column = ({
  column,
  removeColumn,
  getTaskColor,
  removeTask,
}: ColumnProps) => (
  <Card className="w-[350px] flex-shrink-0 bg-white/70 backdrop-blur-md shadow-xl rounded-xl overflow-hidden border-none">
    <CardHeader className="flex flex-row items-center justify-between bg-gradient-to-r from-indigo-400 to-blue-400 text-white">
      <CardTitle className="text-lg">{column.title}</CardTitle>
      <div className="flex gap-1">
        <Button
          variant="ghost"
          size="icon"
          // onClick={() => removeColumn(column.id)}
          className="text-green-200"
        >
          <FilePenLine className="h-4 w-4" />
        </Button>
        <Button
          variant="ghost"
          size="icon"
          onClick={() => removeColumn(column.id)}
          className="text-white"
        >
          <X className="h-4 w-4" />
        </Button>
      </div>
    </CardHeader>
    <CardContent className="h-[500px] p-4">
      <EisenhowerMatrix
        columnId={column.id}
        tasks={column.tasks}
        getTaskColor={getTaskColor}
        removeTask={removeTask}
      />
    </CardContent>
  </Card>
);
