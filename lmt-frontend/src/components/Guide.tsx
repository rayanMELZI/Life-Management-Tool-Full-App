import { Button } from "@/components/ui/button";

type GuideProps = {
  onClose: () => void;
};

const quadrants = [
  {
    title: "Do",
    subtitle: "Important & urgent",
    description: "Deadlines, emergencies, real problems. Do these first.",
    color: "bg-gradient-to-br from-red-200 to-orange-200",
  },
  {
    title: "Plan",
    subtitle: "Important, not urgent",
    description:
      "Goals, learning, health, relationships. Schedule a time for these.",
    color: "bg-gradient-to-br from-yellow-200 to-amber-200",
  },
  {
    title: "Delegate",
    subtitle: "Urgent, not important",
    description:
      "Interruptions and busywork. Hand them off or keep them short.",
    color: "bg-gradient-to-br from-blue-200 to-cyan-200",
  },
  {
    title: "Eliminate",
    subtitle: "Neither important nor urgent",
    description: "Time wasters. Consider dropping them altogether.",
    color: "bg-gradient-to-br from-green-200 to-emerald-200",
  },
];

export const Guide = ({ onClose }: GuideProps) => (
  <div
    className="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4"
    onClick={onClose}
  >
    <div
      className="bg-white rounded-2xl shadow-2xl w-full max-w-lg max-h-[85vh] overflow-y-auto p-6"
      onClick={(event) => event.stopPropagation()}
    >
      <h2 className="text-2xl font-bold mb-1 bg-clip-text text-transparent bg-gradient-to-r from-purple-600 to-blue-600">
        Welcome to LMT
      </h2>
      <p className="text-sm text-gray-600 mb-4">
        A quick tour of how to organize your life here.
      </p>

      <ol className="list-decimal list-inside space-y-2 text-sm text-gray-700 mb-5">
        <li>
          <span className="font-semibold">Create a column</span> for each area
          of your life: work, studies, home, health...
        </li>
        <li>
          <span className="font-semibold">Add tasks</span> to a column, choosing
          how important and how urgent each one is.
        </li>
        <li>
          <span className="font-semibold">Drag tasks</span> between quadrants
          whenever priorities change — everything is saved automatically.
        </li>
      </ol>

      <h3 className="font-semibold text-gray-800 mb-2">
        The Eisenhower matrix
      </h3>
      <p className="text-sm text-gray-600 mb-3">
        Each column sorts its tasks into four quadrants, based on the method
        popularized by president Dwight D. Eisenhower:
      </p>

      <div className="grid grid-cols-2 gap-2 mb-5">
        {quadrants.map((quadrant) => (
          <div key={quadrant.title} className={`rounded-lg p-3 ${quadrant.color}`}>
            <p className="font-bold text-sm">{quadrant.title}</p>
            <p className="text-xs font-semibold text-gray-700 mb-1">
              {quadrant.subtitle}
            </p>
            <p className="text-xs text-gray-700">{quadrant.description}</p>
          </div>
        ))}
      </div>

      <Button
        onClick={onClose}
        className="w-full bg-gradient-to-r from-purple-500 to-blue-500 hover:from-purple-600 hover:to-blue-600 text-white"
      >
        Got it, let's start!
      </Button>
    </div>
  </div>
);
