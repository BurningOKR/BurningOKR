import { CurrentNavigationService } from './current-navigation.service';
import { OkrUnitRole, OkrUnitSchema } from '../shared/model/ui/okr-unit-schema';

describe('CurrentNavigationService', () => {

  let currentNavigationService: CurrentNavigationService;

  const mockSchema5: OkrUnitSchema = {
    id: 5,
    name: 'Schema5Name',
    subDepartments: [],
    userRole: OkrUnitRole.USER,
    isActive: true,
    isTeam: false,
  };
  const mockSchema1: OkrUnitSchema = {
    id: 1,
    name: 'Schema1Name',
    subDepartments: [],
    userRole: OkrUnitRole.USER,
    isActive: true,
    isTeam: false,
  };
  const mockSchema2: OkrUnitSchema = {
    id: 2,
    name: 'Schema2Name',
    subDepartments: [mockSchema5],
    userRole: OkrUnitRole.USER,
    isActive: true,
    isTeam: false,
  };
  const mockSchema3: OkrUnitSchema = {
    id: 3,
    name: 'Schema3Name',
    subDepartments: [],
    userRole: OkrUnitRole.USER,
    isActive: true,
    isTeam: false,
  };
  const mockSchema4: OkrUnitSchema = {
    id: 4,
    name: 'Schema4Name',
    subDepartments: [mockSchema2, mockSchema3],
    userRole: OkrUnitRole.USER,
    isActive: true,
    isTeam: false,
  };
  const mockSchema6: OkrUnitSchema = {
    id: 6,
    name: 'Schema6Name',
    subDepartments: [],
    userRole: OkrUnitRole.USER,
    isActive: true,
    isTeam: false,
  };

  beforeEach(() => {
    currentNavigationService = new CurrentNavigationService();

  });

  it('getClosedStructures should return 0 closed Structures when we dont added any', () => {
    const expected: number[] = [];
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

  it('getClosedStructures should return closed Structures when we mark the structures as closed', () => {
    const expected: number[] = [1, 2, 5, 3];
    currentNavigationService.markStructureAsClosed(mockSchema1);
    currentNavigationService.markStructureAsClosed(mockSchema2);
    currentNavigationService.markStructureAsClosed(mockSchema3);
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

  it(
    'getClosedStructures should return closed Structures and their subDepartments when we mark the structures as closed',
    () => {
      const expected: number[] = [1, 4, 2, 5, 3];
      currentNavigationService.markStructureAsClosed(mockSchema1);
      currentNavigationService.markStructureAsClosed(mockSchema4);
      const actual: number[] = currentNavigationService.getClosedStructures();

      expect(expected).toEqual(actual);
    },
  );

  it('markStructureAsClosed should only mark the structure if it is not already marked', () => {
    const expected: number[] = [1];
    currentNavigationService.markStructureAsClosed(mockSchema1);
    currentNavigationService.markStructureAsClosed(mockSchema1);
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

  it('getClosedStructures should return fewer closed Structures when we mark them as open', () => {
    const expected: number[] = [1];
    currentNavigationService.markStructureAsClosed(mockSchema1);
    currentNavigationService.markStructureAsClosed(mockSchema6);
    currentNavigationService.markStructureAsOpen(mockSchema6.id);
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

  it('isStructureMarkedAsClosed should return true if a structure is marked as closed', () => {
    const expected: boolean = true;
    currentNavigationService.markStructureAsClosed(mockSchema1);
    const actual: boolean = currentNavigationService.isStructureMarkedAsClosed(mockSchema1.id);

    expect(expected).toEqual(actual);
  });

  it('isStructureMarkedAsClosed should return false if a structure is not marked as closed', () => {
    const expected: boolean = false;
    const actual: boolean = currentNavigationService.isStructureMarkedAsClosed(mockSchema1.id);

    expect(expected).toEqual(actual);
  });

  it('isStructureSelected should return true if a structure is selected', () => {
    const expected: boolean = true;
    currentNavigationService.markStructureAsSelected(1);
    const actual: boolean = currentNavigationService.isStructureSelected(1);

    expect(expected).toEqual(actual);
  });

  it('isStructureSelected should return false if a structure is not selected', () => {
    const expected: boolean = false;
    const actual: boolean = currentNavigationService.isStructureSelected(1);

    expect(expected).toEqual(actual);
  });

  it('markStructureAsClosed with undefined or null as parameter throws no error', () => {
    const expected: number[] = [];
    currentNavigationService.markStructureAsClosed(undefined);
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

  it('markStructureAsOpen with undefined or null as parameter throws no error', () => {
    const expected: number[] = [];
    currentNavigationService.markStructureAsOpen(null);
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

  it('markStructureAsOpen when the structur is already open does nothing', () => {
    const expected: number[] = [];
    currentNavigationService.markStructureAsOpen(1);
    const actual: number[] = currentNavigationService.getClosedStructures();

    expect(expected).toEqual(actual);
  });

});
