package mockdata.peoplesoft.model;

import java.util.HashMap;

public class Subjects {
    private static HashMap<String, Subject> set = new HashMap<>();

    public static void add(String name, String shortName) {
        if (!set.containsKey(shortName)) set.put(shortName, new Subject(name, shortName));
    }

    public static Subject get(String name) {
        return set.get(name);
    }

    public static class Subject {
        @Override
        public String toString() {
            return "Subject [name=" + name + ", shortName=" + shortName + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Subject other = (Subject) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (shortName == null) {
                if (other.shortName != null)
                    return false;
            } else if (!shortName.equals(other.shortName))
                return false;
            return true;
        }
        private String name;
        private String shortName;

        private Subject(String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
        }

        public String name() { return name; }
        public String shortName() { return shortName; }
    }
}
