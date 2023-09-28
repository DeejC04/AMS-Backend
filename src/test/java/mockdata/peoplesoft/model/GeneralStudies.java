package mockdata.peoplesoft.model;

import java.util.HashMap;

public class GeneralStudies {
    public static HashMap<String, GeneralStudy> set = new HashMap<>();

    public static void add(String name) {
        if (!set.containsKey(name)) set.put(name, new GeneralStudy(name));
    }

    public static GeneralStudy get(String name) {
        return set.get(name);
    }

    public static class GeneralStudy {
        private String name;

        private GeneralStudy(String name) {
            this.name = name;
        }

        public String name() { return name; }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
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
            GeneralStudy other = (GeneralStudy) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "GeneralStudy [name=" + name + "]";
        }
    }
}
