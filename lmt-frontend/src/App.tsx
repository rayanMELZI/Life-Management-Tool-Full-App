import { useState, useEffect } from "react";
import { DragDropContext, DropResult } from "react-beautiful-dnd";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Button } from "@/components/ui/button";
import { TaskInput } from "./components/TaskInput.tsx";
import { ColumnInput } from "./components/ColumnInput.tsx";
import { Column } from "./components/Column.tsx";
import { AuthPage } from "./components/AuthPage.tsx";
import { Guide } from "./components/Guide.tsx";

type Task = {
  id: number;
  content: string;
  importance: "high" | "low";
  urgency: "urgent" | "not urgent";
};

type ColumnType = {
  id: number;
  title: string;
  tasks: Task[];
};

type Auth = {
  username: string;
  token: string;
};

const AUTH_STORAGE_KEY = "lmt-auth";
const GUIDE_SEEN_KEY = "lmt-guide-seen";

export default function App() {
  // const domain = "http://localhost:8080";
  // const domain = "https://life-management-tool-server.onrender.com";
  const domain = import.meta.env.VITE_BACKEND_DOMAIN;

  const [columns, setColumns] = useState<ColumnType[]>([
    // { id: 0, title: "LMT", tasks: [] },
  ]);

  const [newTask, setNewTask] = useState("");
  const [selectedColumn, setSelectedColumn] = useState(0);
  const [importance, setImportance] = useState<"high" | "low">("high");
  const [urgency, setUrgency] = useState<"urgent" | "not urgent">("urgent");
  const [newColumnTitle, setNewColumnTitle] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const [auth, setAuth] = useState<Auth | null>(() => {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    return stored ? (JSON.parse(stored) as Auth) : null;
  });

  // fetch wrapper that sends the token and signs out when it is rejected
  const apiFetch = async (path: string, options: RequestInit = {}) => {
    const response = await fetch(`${domain}${path}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(auth ? { Authorization: `Bearer ${auth.token}` } : {}),
        ...options.headers,
      },
    });
    if (response.status === 401) {
      localStorage.removeItem(AUTH_STORAGE_KEY);
      setAuth(null);
      setColumns([]);
      throw new Error("Session expired");
    }
    return response;
  };

  // show the guide automatically the first time someone signs in
  const [showGuide, setShowGuide] = useState(false);
  useEffect(() => {
    if (auth && !localStorage.getItem(GUIDE_SEEN_KEY)) {
      setShowGuide(true);
    }
  }, [auth]);

  const closeGuide = () => {
    localStorage.setItem(GUIDE_SEEN_KEY, "1");
    setShowGuide(false);
  };

  const handleAuth = (newAuth: Auth) => {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(newAuth));
    setAuth(newAuth);
  };

  const handleLogout = async () => {
    try {
      await apiFetch("/api/auth/logout", { method: "POST" });
    } catch (error) {
      console.error("Failed to log out on the server:", error);
    }
    localStorage.removeItem(AUTH_STORAGE_KEY);
    setAuth(null);
    setColumns([]);
    setErrorMessage("");
  };

  const addTask = async () => {
    if (newTask.trim() === "") return;
    if (!columns.some((column) => column.id === selectedColumn)) return;

    // interaction with database
    try {
      const response = await apiFetch("/api/task/add", {
        method: "POST",
        body: JSON.stringify({
          content: newTask,
          importance,
          urgency,
          columnId: selectedColumn,
        }),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const savedTask: Task = await response.json();

      setColumns(
        columns.map((column) =>
          column.id === selectedColumn
            ? { ...column, tasks: [...column.tasks, savedTask] }
            : column
        )
      );
      setNewTask("");
      setErrorMessage("");
    } catch (error) {
      console.error("Failed to add task to database:", error);
      setErrorMessage("Could not add the task. Please try again.");
    }
  };

  const removeTask = (columnId: number, taskId: number) => {
    // Show confirmation dialog
    const isConfirmed = globalThis.confirm(
      "Are you sure you want to delete this task?"
    );

    if (!isConfirmed) return; // If the user cancels, stop the deletion.

    // interaction with database
    const removeTaskFromDatabase = async () => {
      try {
        const response = await apiFetch("/api/task/delete", {
          method: "DELETE",
          body: JSON.stringify({ id: taskId }),
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
      } catch (error) {
        console.error("Failed to delete task from database:", error);
        setErrorMessage("Could not delete the task. Please try again.");
      }
    };
    removeTaskFromDatabase();

    const updatedColumns = columns.map((column) => {
      if (column.id === columnId) {
        return {
          ...column,
          tasks: column.tasks.filter((task) => task.id !== taskId),
        };
      }
      return column;
    });

    setColumns(updatedColumns);
  };

  // adds a new column
  const addColumn = async () => {
    if (newColumnTitle.trim() === "") return;

    // interaction with database
    try {
      const response = await apiFetch("/api/domainColumn/add", {
        method: "POST",
        body: JSON.stringify({ title: newColumnTitle }),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const savedColumn: ColumnType = await response.json();
      savedColumn.tasks = savedColumn.tasks ?? [];

      setColumns([...columns, savedColumn]);
      setNewColumnTitle("");
      setSelectedColumn(savedColumn.id);
      setErrorMessage("");
    } catch (error) {
      console.error("Failed to add column to database:", error);
      setErrorMessage("Could not add the column. Please try again.");
    }
  };

  // renames a column
  const renameColumn = async (columnId: number) => {
    const column = columns.find((column) => column.id === columnId);
    if (!column) return;

    const newTitle = globalThis.prompt("New column title:", column.title);
    if (!newTitle || newTitle.trim() === "") return;

    // interaction with database
    try {
      const response = await apiFetch("/api/domainColumn/update", {
        method: "PUT",
        body: JSON.stringify({ id: columnId, title: newTitle.trim() }),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      setColumns(
        columns.map((column) =>
          column.id === columnId
            ? { ...column, title: newTitle.trim() }
            : column
        )
      );
    } catch (error) {
      console.error("Failed to rename column in database:", error);
      setErrorMessage("Could not rename the column. Please try again.");
    }
  };

  const removeColumn = (columnId: number) => {
    // Show confirmation dialog
    const isConfirmed = globalThis.confirm(
      "Are you sure you want to delete this column and all of its tasks?"
    );

    if (!isConfirmed) return; // If the user cancels, stop the deletion.

    // interaction with database
    const removeColumnFromDatabase = async () => {
      try {
        const response = await apiFetch("/api/domainColumn/delete", {
          method: "DELETE",
          body: JSON.stringify({ id: columnId }),
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const res = await response.text();
        console.log(res);
      } catch (error) {
        console.error("Failed to delete column to database:", error);
        setErrorMessage("Could not delete the column. Please try again.");
      }
    };
    removeColumnFromDatabase();

    setColumns(columns.filter((column) => column.id !== columnId));
  };

  // Load the signed-in user's columns from the database
  useEffect(() => {
    if (!auth) return;

    const fetchColumnsData = async () => {
      setIsLoading(true);
      try {
        const response = await apiFetch("/api/domainColumn/all");
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data: ColumnType[] = await response.json();
        setColumns(data);
        if (data.length > 0) {
          setSelectedColumn(data[0].id);
        }
      } catch (error) {
        console.error("Failed to fetch columns data:", error);
        setErrorMessage(
          "Could not load your columns. Please check your connection and refresh the page."
        );
      } finally {
        setIsLoading(false);
      }
    };

    fetchColumnsData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [auth?.token]);

  const onDragEnd = (result: DropResult) => {
    if (!result.destination) return;

    const sourceColumn = columns.find(
      (column) => column.id === Number(result.source.droppableId.split("-")[0])
    );
    const destColumn = columns.find(
      (column) =>
        result.destination &&
        column.id === Number(result.destination.droppableId.split("-")[0])
    );
    const [reorderedItem] = sourceColumn!.tasks.splice(result.source.index, 1);

    const [, destImportance, destUrgency] =
      result.destination.droppableId.split("-");
    reorderedItem.importance = destImportance as "high" | "low";
    reorderedItem.urgency = destUrgency as "urgent" | "not urgent";

    destColumn!.tasks.push(reorderedItem);

    setColumns([...columns]);

    // interaction with database
    const updateTaskInDatabase = async () => {
      try {
        const response = await apiFetch("/api/task/update", {
          method: "PUT",
          body: JSON.stringify({
            id: reorderedItem.id,
            content: reorderedItem.content,
            importance: reorderedItem.importance,
            urgency: reorderedItem.urgency,
            columnId: destColumn!.id,
          }),
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
      } catch (error) {
        console.error("Failed to update task in database:", error);
        setErrorMessage("Could not save the task's new position. Please try again.");
      }
    };
    updateTaskInDatabase();
  };

  const getTaskColor = (importance: string, urgency: string) => {
    if (importance === "high" && urgency === "urgent")
      return "bg-gradient-to-r from-red-500 to-orange-500 text-white";
    if (importance === "high" && urgency === "not urgent")
      return "bg-gradient-to-r from-yellow-400 to-amber-500 text-white";
    if (importance === "low" && urgency === "urgent")
      return "bg-gradient-to-r from-blue-500 to-cyan-500 text-white";
    return "bg-gradient-to-r from-green-500 to-emerald-500 text-white";
  };

  if (!auth) {
    return <AuthPage domain={domain} onAuth={handleAuth} />;
  }

  return (
    <div className="p-4 min-h-screen bg-gradient-to-br from-purple-100 via-indigo-100 to-blue-100">
      {showGuide && <Guide onClose={closeGuide} />}
      <div className="flex justify-end items-center gap-3 mb-2">
        <span className="text-sm text-gray-600">
          Signed in as <span className="font-semibold">{auth.username}</span>
        </span>
        <Button
          variant="outline"
          size="sm"
          onClick={() => setShowGuide(true)}
          className="bg-white/50 border-indigo-200"
        >
          Guide
        </Button>
        <Button
          variant="outline"
          size="sm"
          onClick={handleLogout}
          className="bg-white/50 border-indigo-200"
        >
          Sign Out
        </Button>
      </div>

      <h1 className="text-4xl font-bold mb-6 text-center bg-clip-text text-transparent bg-gradient-to-r from-purple-600 to-blue-600">
        Life Management Tool
      </h1>

      {/* Error banner */}
      {errorMessage && (
        <div className="mb-4 mx-auto max-w-xl rounded-lg bg-red-100 border border-red-300 text-red-700 px-4 py-2 text-center text-sm">
          {errorMessage}
        </div>
      )}

      {/* Task Input */}
      <TaskInput
        newTask={newTask}
        setNewTask={setNewTask}
        selectedColumn={selectedColumn.toString()}
        setSelectedColumn={(column: string) =>
          setSelectedColumn(Number(column))
        }
        columns={columns}
        importance={importance}
        setImportance={setImportance}
        urgency={urgency}
        setUrgency={setUrgency}
        addTask={addTask}
      />

      {/* Column Input */}
      <ColumnInput
        newColumnTitle={newColumnTitle}
        setNewColumnTitle={setNewColumnTitle}
        addColumn={addColumn}
      />

      {/* Columns with Drag & Drop */}
      <DragDropContext onDragEnd={onDragEnd}>
        <ScrollArea className="w-full">
          {isLoading ? (
            <div className="text-xl text-indigo-500 font-semibold mt-16 mx-auto text-center">
              Loading your columns...
            </div>
          ) : columns.length < 1 ? (
            <div className="text-3xl text-white font-semibold bg-gradient-to-tl from-teal-400 to-cyan-400 mt-16 mx-auto text-center w-fit p-32 rounded-3xl">
              Add a Column to start the Management process
            </div>
          ) : (
            <div className="flex space-x-4 pb-4 px-4 overflow-x-scroll">
              {columns.map((column) => (
                <Column
                  key={column.id}
                  column={column}
                  removeColumn={removeColumn}
                  renameColumn={renameColumn}
                  removeTask={removeTask}
                  getTaskColor={getTaskColor}
                />
              ))}
            </div>
          )}
        </ScrollArea>
      </DragDropContext>
    </div>
  );
}
