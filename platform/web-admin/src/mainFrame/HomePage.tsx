import { initStateSetters } from 'admin/src/common/component';
import { HomePageSkeleton } from 'admin/src-gen/mainFrame/HomePageSkeleton';

function HomePageFC(props: { element: HomePageComponent }) {
    initStateSetters(props.element);
    return <div>Home</div>;
}

export class HomePageComponent extends HomePageSkeleton {
    functionalComponent = HomePageFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
